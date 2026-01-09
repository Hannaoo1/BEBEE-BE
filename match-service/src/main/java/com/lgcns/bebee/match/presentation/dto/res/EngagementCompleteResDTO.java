package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.CompleteEngagementUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "활동 완료 체크 응답")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EngagementCompleteResDTO {
    @Schema(description = "마지막 활동 여부 (DAY는 항상 true)",
            example = "true")
    private Boolean isLastActivity;

    public static EngagementCompleteResDTO of(Boolean isLastActivity) {
        return new EngagementCompleteResDTO(isLastActivity);
    }

    public static EngagementCompleteResDTO from(CompleteEngagementUseCase.Result result) {
        return new EngagementCompleteResDTO(
                result.getIsLastEngagement()
        );
    }
}