package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

// 리뷰 작성 요청 DTO
@Schema(description = "리뷰 작성 요청")
@Getter
@NoArgsConstructor
public class ReviewCreateReqDTO {

    @Schema(description = "활동 ID", example = "123", required = true)
    @NotNull(message = "활동 ID는 필수입니다")
    private String engagementId;

    @Schema(
            description = "선택한 키워드 ID 리스트 (1~24)",
            example = "[1, 3, 5]",
            required = true
    )
    @NotEmpty(message = "최소 1개 이상의 키워드를 선택해야 합니다")
    private List<Integer> keywordIds;

    // DTO → UseCase Param 변환
    public CreateReviewUseCase.Param toParam(Long currentMemberId) {
        return new CreateReviewUseCase.Param(
                Long.parseLong(engagementId),
                currentMemberId,
                keywordIds
        );
    }
}