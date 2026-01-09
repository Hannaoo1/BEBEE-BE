package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetHelperBadgesUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperBadgesGetResDTO {

    private List<BadgeStatusDTO> badges;

    public static HelperBadgesGetResDTO from(GetHelperBadgesUseCase.Result result) {
        List<BadgeStatusDTO> badges = result.getBadges().stream()
                .map(badge -> badge)
                .toList();

        return new HelperBadgesGetResDTO(badges);
    }
}