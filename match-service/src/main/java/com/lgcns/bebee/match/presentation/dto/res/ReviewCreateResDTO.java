package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "리뷰 생성 응답")
public class ReviewCreateResDTO {

    @Schema(description = "생성된 리뷰 ID", example = "7")
    private Long reviewId;

    public static ReviewCreateResDTO from(CreateReviewUseCase.Result result) {
        return new ReviewCreateResDTO(result.getReviewId());
    }
}
