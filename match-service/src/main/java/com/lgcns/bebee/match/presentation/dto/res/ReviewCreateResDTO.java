package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 리뷰 작성 응답 DTO
@Schema(description = "리뷰 작성 응답")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewCreateResDTO {

    @Schema(description = "생성된 리뷰 ID", example = "456")
    private String reviewId;

    //Result → DTO 변환
    public static ReviewCreateResDTO from(CreateReviewUseCase.Result result) {
        return new ReviewCreateResDTO(
                String.valueOf(result.getReviewId())
        );
    }
}