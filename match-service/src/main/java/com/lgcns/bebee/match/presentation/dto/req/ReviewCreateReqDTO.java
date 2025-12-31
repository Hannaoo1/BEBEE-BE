package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "리뷰 작성 요청")
public class ReviewCreateReqDTO {

    @NotNull(message = "활동 ID는 필수입니다")
    @Schema(description = "활동 ID", example = "1", required = true)
    private Long engagementId;

    @NotNull(message = "키워드는 최소 1개 이상 선택해야 합니다")
    @Schema(description = "선택한 키워드 ID 목록", example = "[1, 3, 5, 7, 9]", required = true)
    private List<Integer> keywordIds;

    public CreateReviewUseCase.Param toParam(Long memberId) {
        return new CreateReviewUseCase.Param(
                this.engagementId,
                memberId,
                this.keywordIds
        );
    }
}
