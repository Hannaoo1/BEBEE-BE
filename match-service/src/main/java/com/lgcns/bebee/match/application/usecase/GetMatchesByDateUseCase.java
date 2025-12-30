package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.domain.entity.*;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import com.lgcns.bebee.match.domain.service.MemberReader;
import com.lgcns.bebee.match.domain.service.PostManager;
import com.lgcns.bebee.match.presentation.dto.DayEngagementTimeDTO;
import com.lgcns.bebee.match.presentation.dto.MemberInfoDTO;
import com.lgcns.bebee.match.presentation.dto.TermEngagementTimeDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementHelpCategoryDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMatchesByDateUseCase implements UseCase<GetMatchesByDateUseCase.Param, GetMatchesByDateUseCase.Result> {

    private final MatchRepository matchRepository;
    private final MemberReader memberReader;
    private final PostManager postManager;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {
        DayOfWeek dayOfWeek = param.getDate().getDayOfWeek();

        // Match + Agreement를 한 번에 조회 (JOIN FETCH)
        List<Match> matches = matchRepository.findByDateAndMember(
                param.getMemberId(),
                param.getDate(),
                dayOfWeek,
                param.getEngagementType()
        );

        return Result.from(matches, memberReader, postManager);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final LocalDate date;
        private final EngagementType engagementType;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private List<MatchInfo> matchInfos;

        public static Result from(
                List<Match> matches,
                MemberReader memberReader,
                PostManager postManager
        ) {
            List<MatchInfo> matchInfos = matches.stream()
                    .map(match -> {
                        // Match에서 Agreement 가져오기 (이미 JOIN FETCH로 로드됨!)
                        Agreement agreement = match.getAgreement();

                        MatchMemberSync helper = memberReader.getById(agreement.getHelperId());
                        MatchMemberSync disabled = memberReader.getById(agreement.getDisabledId());

                        String title = match.getTitle();
                        Long chatRoomId = match.getChatRoomId();

                        String thumbnailImageUrl = null;
                        Post post = postManager.findSinglePost(agreement.getPostId());
                        List<PostImage> postImages = post.getImages();
                        if (postImages != null && !postImages.isEmpty()) {
                            thumbnailImageUrl = postImages.get(0).getImageUrl();
                        }

                        List<AgreementHelpCategoryDTO> categoryDTOs = agreement.getHelpCategories().stream()
                                .map(AgreementHelpCategoryDTO::from)
                                .toList();

                        Object engagementTime = null;
                        if (agreement.getType() == EngagementType.DAY) {
                            engagementTime = DayEngagementTimeDTO.from(
                                    agreement.getPeriod(),
                                    agreement.getSchedules().get(0)
                            );
                        } else if (agreement.getType() == EngagementType.TERM) {
                            engagementTime = TermEngagementTimeDTO.from(
                                    agreement.getPeriod(),
                                    agreement.getSchedules()
                            );
                        }

                        return new MatchInfo(
                                String.valueOf(agreement.getId()),
                                String.valueOf(agreement.getPostId()),
                                title,
                                thumbnailImageUrl,
                                MemberInfoDTO.from(helper),
                                MemberInfoDTO.from(disabled),
                                agreement.getConfirmationDate(),
                                agreement.getType(),
                                categoryDTOs,
                                agreement.getIsVolunteer(),
                                agreement.getUnitHoney(),
                                agreement.getTotalHoney(),
                                agreement.getRegion(),
                                engagementTime,
                                agreement.getIsDayComplete(),
                                agreement.getIsTermComplete(),
                                String.valueOf(chatRoomId)
                        );
                    }).toList();

            return new Result(matchInfos);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class MatchInfo {
        private String agreementId;
        private String postId;
        private String title;
        private String thumbnailImageUrl;
        private MemberInfoDTO helper;
        private MemberInfoDTO disabled;
        private LocalDate confirmationDate;
        private EngagementType type;
        private List<AgreementHelpCategoryDTO> helpCategories;
        private Boolean isVolunteer;
        private Integer unitHoney;
        private Integer totalHoney;
        private String region;
        private Object engagementTime;
        private Boolean isDayComplete;
        private Boolean isTermComplete;
        private String chatRoomId;
    }
}

