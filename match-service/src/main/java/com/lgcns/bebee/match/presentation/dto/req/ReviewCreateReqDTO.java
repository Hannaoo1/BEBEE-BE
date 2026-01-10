package com.lgcns.bebee.match.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateReqDTO {

    @Schema(
            description = "활동 ID (EngagementId - 마지막 활동만 리뷰 가능)",
            example = "500",
            required = true
    )
    @NotNull(message = "활동 ID는 필수입니다")
    private String engagementId;

    @Schema(
            description = "리뷰 대상자 ID (후기를 받는 회원)",
            example = "800",
            required = true
    )
    @NotNull(message = "리뷰 대상자는 필수입니다")
    private String revieweeId;

    @Schema(
            description = "선택한 키워드 ID 목록 (여러 개 가능!)",
            example = "[1, 3, 5]",
            required = true
    )
    @NotEmpty(message = "키워드는 최소 1개 이상 선택해야 합니다")
    private List<String> keywordIds;

}