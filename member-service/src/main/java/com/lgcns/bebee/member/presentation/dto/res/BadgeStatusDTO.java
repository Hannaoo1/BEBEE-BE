package com.lgcns.bebee.member.presentation.dto.res;

import com.lgcns.bebee.member.application.usecase.GetHelperBadgesUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 뱃지 상태 DTO
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "뱃지 상태 DTO")
public class BadgeStatusDTO {

    @Schema(description = "장애 유형 ID", example = "1")
    private Long disabilityCategoryId;

    @Schema(description = "활동 완료 횟수", example = "10")
    private Integer count;

    @Schema(description = "뱃지 코드 (null, LEVEL_1, LEVEL_2)", example = "LEVEL_1")
    private String badgeCode;

    // BadgeStatusInfo -> DTO 변환
    public static BadgeStatusDTO from(GetHelperBadgesUseCase.BadgeStatusInfo info) {
        return new BadgeStatusDTO(
                info.getDisabilityCategoryId(),
                info.getCount(),
                info.getBadgeCode()
        );
    }
}
