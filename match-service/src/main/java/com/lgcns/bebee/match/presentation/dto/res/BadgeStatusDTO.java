package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.domain.entity.Badge;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BadgeStatusDTO {
    private List<Integer> disabilityCategoryIds;
    private Integer completionCount;
    private String badgeCode;

    public static BadgeStatusDTO from(Badge badge) {
        return new BadgeStatusDTO(
                List.of(badge.getDisabilityCategoryId().intValue()),
                badge.getCompletionCount(),
                badge.getBadgeCode()
        );
    }

    // 빈 뱃지 생성 (count=0, code=null)
    public static BadgeStatusDTO ofEmpty(Long disabilityCategoryId) {
        return new BadgeStatusDTO(
                List.of(disabilityCategoryId.intValue()),
                0,
                null
        );
    }
}
