package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateReqDTO {

    @Schema(description = "리뷰 대상자 ID", example = "800", required = true)
    @NotNull(message = "리뷰 대상자는 필수입니다")
    private String revieweeId;

    @Schema(description = "선택한 키워드 ID 목록", example = "[1, 3, 5]", required = true)
    @NotEmpty(message = "키워드는 최소 1개 이상 선택해야 합니다")
    private List<String> keywordIds;

    public CreateReviewUseCase.Param toParam(Long currentMemberId) {

        // String → Integer 변환
        List<Integer> keywordIdInts = keywordIds.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        return new CreateReviewUseCase.Param(
                currentMemberId,
                Long.parseLong(revieweeId),
                keywordIdInts
        );
    }
}