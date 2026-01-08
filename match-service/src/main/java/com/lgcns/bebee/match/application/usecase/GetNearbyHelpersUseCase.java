package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.util.AgeGroupCalculator;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.LocationSearchType;
import com.lgcns.bebee.match.domain.repository.MemberRepository;
import com.lgcns.bebee.match.domain.service.MemberManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetNearbyHelpersUseCase implements UseCase<GetNearbyHelpersUseCase.Param, GetNearbyHelpersUseCase.Result> {
    private final MemberManager memberManager;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Param params) {
        MemberSync currentMember = memberManager.findExistingMember(params.currentMemberId);

        if(currentMember.getRole() != Role.DISABLED){
            throw MatchErrors.ONLY_DISABLED_MEMBERS_ALLOWED.toException();
        }

        List<MemberSync> nearbyHelpers = findNearbyHelpers(currentMember, params.type, params.longitude, params.latitude, params.radius);

        return Result.from(nearbyHelpers);
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

        public static Result from(List<MemberSync> nearbyHelpers){
            List<MemberDTO> members = nearbyHelpers.stream()
                    .map(MemberDTO::from)
                    .toList();

            return new Result(members);
        }

        @Getter
        @RequiredArgsConstructor
        public static class MemberDTO{
            private final Long id;
            private final String nickname;
            private final String gender;
            private final Integer ageGroup;
            private final Double latitude;
            private final Double longitude;
            private final List<Long> helpCategories;

            public static MemberDTO from(MemberSync member) {
                List<Long> helpCategoryIds = member.getHelpCategories()
                        .stream()
                        .map(mhc -> mhc.getId().getHelpCategoryId())
                        .toList();

                return new MemberDTO(
                        member.getId(),
                        member.getNickname(), member.getRole().name(),
                        AgeGroupCalculator.calculateAgeGroup(member.getBirthDate()),
                        member.getLatitude(), member.getLongitude(),
                        helpCategoryIds
                        );
            }
        }
    }
}
