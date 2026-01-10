package com.lgcns.bebee.match.presentation.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperBadgesGetResDTO {
    private List<BadgeStatusDTO> badges;

    public static HelperBadgesGetResDTO from(List<BadgeStatusDTO> badges) {
        return new HelperBadgesGetResDTO(badges);
    }
}
