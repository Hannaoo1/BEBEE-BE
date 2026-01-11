package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetHelperBadgesUseCase;
import com.lgcns.bebee.match.domain.entity.Badge;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperBadgesGetResDTO {
    private List<BadgeStatusDTO> badges;

    public static HelperBadgesGetResDTO from(GetHelperBadgesUseCase.Result result) {
        List<BadgeStatusDTO> badgeStatuses = new ArrayList<>();

        // 존재하는 뱃지를 DTO로 변환
        for (Badge badge : result.getExistingBadges()) {
            badgeStatuses.add(BadgeStatusDTO.from(badge));
        }

        // 없는 카테고리에 대해 빈 뱃지 생성
        for (Long categoryId : result.getMissingCategoryIds()) {
            badgeStatuses.add(BadgeStatusDTO.ofEmpty(categoryId));
        }

        return new HelperBadgesGetResDTO(badgeStatuses);
    }
}
