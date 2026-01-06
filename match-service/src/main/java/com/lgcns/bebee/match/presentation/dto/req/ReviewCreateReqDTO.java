package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateReqDTO {

    @NotEmpty(message = "키워드는 최소 1개 이상 선택해야 합니다")
    private List<Integer> keywordIds;

    public CreateReviewUseCase.Param toParam(Long currentMemberId) {
        return new CreateReviewUseCase.Param(
                currentMemberId,
                keywordIds
        );
    }
}