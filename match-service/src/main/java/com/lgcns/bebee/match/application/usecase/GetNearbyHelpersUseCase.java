package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.util.AgeGroupCalculator;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.LocationSearchType;
import com.lgcns.bebee.match.domain.repository.MemberRepository;
import com.lgcns.bebee.match.domain.service.BadgeManager;
import com.lgcns.bebee.match.domain.service.MemberManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetNearbyHelpersUseCase implements UseCase<GetNearbyHelpersUseCase.Param, GetNearbyHelpersUseCase.Result> {
    private final MemberManager memberManager;
    private final MemberRepository memberRepository;
    private final BadgeManager badgeManager;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Param params) {
        MemberSync currentMember = memberManager.findExistingMember(params.currentMemberId);

        if(currentMember.getRole() != Role.DISABLED){
            throw MatchErrors.ONLY_DISABLED_MEMBERS_ALLOWED.toException();
        }

        List<MemberSync> nearbyHelpers = findNearbyHelpers(currentMember, params.type, params.longitude, params.latitude, params.radius);

        // 도우미 목록을 MemberDTO로 변환하면서 뱃지 정보 추가
        List<Result.MemberDTO> members = nearbyHelpers.stream()
                .map(member -> {
                    List<Long> helpCategoryIds = member.getHelpCategories()
                            .stream()
                            .map(mhc -> mhc.getId().getHelpCategoryId())
                            .toList();

                    // 도우미의 뱃지 정보 조회
                    List<Result.BadgeInfo> badges = badgeManager.findBadgesByHelperId(member.getId())
                            .stream()
                            .map(badge -> new Result.BadgeInfo(
                                    badge.getDisabilityCategoryId(),
                                    badge.getCompletionCount(),
                                    badge.getBadgeCode()
                            ))
                            .collect(Collectors.toList());

                    return new Result.MemberDTO(
                            member.getId(),
                            member.getNickname(),
                            member.getGender().name(),
                            AgeGroupCalculator.calculateAgeGroup(member.getBirthDate()),
                            member.getLatitude(),
                            member.getLongitude(),
                            helpCategoryIds,
                            badges
                    );
                })
                .collect(Collectors.toList());

        return new Result(members);
    }

    private List<MemberSync> findNearbyHelpers(MemberSync currentMember, LocationSearchType type, Double longitude, Double latitude, Integer radius){
        if(type == LocationSearchType.HOME){
            return memberRepository.findHelpersWithinRadius(currentMember.getLongitude(), currentMember.getLatitude(), radius);
        }

        return memberRepository.findHelpersWithinRadius(longitude, latitude, radius);
    }

    @RequiredArgsConstructor
    public static class Param implements Params{
        private final Long currentMemberId;
        private final LocationSearchType type;
        private final Double latitude;
        private final Double longitude;
        private final Integer radius;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result{
        private final List<MemberDTO> nearbyHelpers;

        @Getter
        @AllArgsConstructor
        public static class MemberDTO{
            private final Long id;
            private final String nickname;
            private final String gender;
            private final Integer ageGroup;
            private final Double latitude;
            private final Double longitude;
            private final List<Long> helpCategories;
            private final List<BadgeInfo> badges;
        }

        @Getter
        @AllArgsConstructor
        public static class BadgeInfo {
            private final Long disabilityCategoryId;
            private final Integer completionCount;
            private final String badgeCode;
        }
    }
}
