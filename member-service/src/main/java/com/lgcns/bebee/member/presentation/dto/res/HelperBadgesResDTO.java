package com.lgcns.bebee.member.presentation.dto.res;

import com.lgcns.bebee.member.application.usecase.GetHelperBadgesUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

// 도우미 뱃지 목록 응답 DTO
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "도우미 뱃지 목록 응답 DTO")
public class HelperBadgesResDTO {

    @Schema(description = "뱃지 상태 목록")
    private List<BadgeStatusDTO> badgeStatus;

    // Result -> DTO 변환
    public static HelperBadgesResDTO from(GetHelperBadgesUseCase.Result result) {
        List<BadgeStatusDTO> badgeStatusList = result.getBadgeStatusList().stream()
                .map(BadgeStatusDTO::from)
                .collect(Collectors.toList());

        return new HelperBadgesResDTO(badgeStatusList);
    }
}
