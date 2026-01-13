package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetNearbyHelpersUseCase;

import java.util.List;

public record NearbyHelpersGetResDTO(
    List<NearbyHelperDTO> nearByHelpers
) {
    public static NearbyHelpersGetResDTO from(GetNearbyHelpersUseCase.Result result) {
        List<NearbyHelperDTO> nearbyHelpersDTO = result.getNearbyHelpers()
                .stream()
                .map(memberDTO -> new NearbyHelperDTO(
                        String.valueOf(memberDTO.getId()),
                        memberDTO.getNickname(),
                        memberDTO.getGender(),
                        memberDTO.getAgeGroup(),
                        memberDTO.getLatitude(),
                        memberDTO.getLongitude(),
                        memberDTO.getHelpCategories(),
                        memberDTO.getBadges().stream()
                                .map(badge -> new BadgeDTO(
                                        badge.getDisabilityCategoryId(),
                                        badge.getCompletionCount(),
                                        badge.getBadgeCode()
                                ))
                                .toList()
                ))
                .toList();

        return new NearbyHelpersGetResDTO(nearbyHelpersDTO);
    }

    public record NearbyHelperDTO(
            String id,
            String nickname,
            String gender,
            Integer ageGroup,
            Double latitude,
            Double longitude,
            List<Long> helpCategories,
            List<BadgeDTO> badges
    ){
    }

    public record BadgeDTO(
            Long disabilityCategoryId,
            Integer completionCount,
            String badgeCode
    ){
    }
}
