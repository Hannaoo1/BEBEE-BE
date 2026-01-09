package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetHelperBadgesUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BadgeStatusDTO {

    private List<Integer> disabilityCategoryIds;
    private Integer count;
    private String badgeCode;

    public static BadgeStatusDTO from(GetHelperBadgesUseCase.BadgeInfo info) {
        return new BadgeStatusDTO(
                List.of(info.getCategoryId().intValue()),
                info.getCount(),
                info.getBadgeCode()
        );
    }
}