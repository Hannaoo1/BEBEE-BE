package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.domain.entity.*;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.EngagementRepository;
import com.lgcns.bebee.match.domain.repository.dto.EngagementSearchCond;
import com.lgcns.bebee.match.domain.service.MemberManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMatchesByDateUseCase implements UseCase<GetMatchesByDateUseCase.Param, GetMatchesByDateUseCase.Result> {
    private final MemberManager memberManager;
    private final EngagementRepository engagementRepository;

    @Override
    public Result execute(Param param) {
        MemberSync member = memberManager.findExistingMember(param.currentMemberId);

        EngagementType type = param.type != null ? EngagementType.from(param.type) : null;
        List<Engagement> engagements = engagementRepository.searchEngagements(
                EngagementSearchCond.from(param.currentMemberId, type, param.date)
        );

        return Result.from(engagements, member, memberManager);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long currentMemberId;
        private final LocalDate date;
        private final String type;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private List<MatchDTO> matches;

        public static Result from(List<Engagement> engagements, MemberSync currentMember, MemberManager memberManager) {
            List<MatchDTO> engagementDTOs = engagements.stream()
                    .map(engagement -> MatchDTO.from(engagement, currentMember, memberManager))
                    .collect(Collectors.toList());

            return new Result(engagementDTOs);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MatchDTO {
        private final Long engagementId;
        private final Long matchId;
        private final Long agreementId;
        private final Long otherId;
        private final String otherNickname;
        private final String thumbnailImageUrl;
        private final String title;
        private final Long chatRoomId;
        private final String region;
        private final String helpType;
        private final LocalDate date;  // DAY 타입일 때만 값 있음
        private final List<String> dayOfWeeks;
        private final List<Long> helpCategoryIds;

        private final String status;

        public static MatchDTO from(Engagement engagement, MemberSync member, MemberManager memberManager) {
            Match match = engagement.getMatch();
            Agreement agreement = match.getAgreement();

            // currentMemberId와 비교하여 otherId 결정
            Long otherId = match.getHelperId().equals(member.getId())
                    ? match.getDisabledId()
                    : match.getHelperId();

            MemberSync otherMember = memberManager.findExistingMember(otherId);

            List<String> dayOfWeeks = agreement.getSchedules().stream()
                    .map(AgreementSchedule::getDayOfWeek)
                    .distinct()
                    .sorted()
                    .map(Enum::name)
                    .toList();

            String status = null;

            if (engagement.getDate().isAfter(LocalDate.now())) {
                status = "INACTIVE";
            }else{
                boolean isHelper = match.getHelperId().equals(member.getId());
                boolean isChecked = isHelper ? engagement.getIsHelperCheck() : engagement.getIsDisabledCheck();
                boolean hasReview = isHelper
                        ? match.getHelperReview() != null
                        : match.getDisabledReview() != null;

                if(!isChecked){
                    status = "ACTIVE";
                }else{
                    LocalDate endDate = agreement.getPeriod().getEndDate();

                    if (engagement.getDate().equals(endDate)) {
                        status = "COMPLETED";
                    }else{

                        if (hasReview) {
                            status = "REVIEW_COMPLETED";
                        }else{
                            status = "REVIEW_ACTIVE";
                        }
                    }
                }
            }

            List<Long> helpCategoryIds = agreement.getHelpCategories().stream()
                    .map(category -> category.getId().getHelpCategoryId())
                    .toList();

            return new MatchDTO(
                    engagement.getId(),
                    match.getMatchId(),
                    agreement.getId(),
                    otherId,
                    otherMember.getNickname(),
                    match.getImageUrl(),
                    match.getTitle(),
                    match.getChatRoomId(),
                    agreement.getRegion(),
                    agreement.getType().name(),
                    engagement.getDate(),
                    dayOfWeeks,
                    helpCategoryIds,
                    status
            );
        }
    }
}

