package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.CompleteEngagementUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "활동 완료 체크 요청")
@Getter
@NoArgsConstructor
public class EngagementCompleteReqDTO {

    @Schema(description = "사용자 유형 (HELPER: 도우미, DISABLED: 장애인)",
            example = "HELPER")
    @NotNull(message = "사용자 유형은 필수입니다")
    private String userType;

    // DTO → Param 변환
    public CompleteEngagementUseCase.Param toParam(String engagementId, String memberId) {
        return new CompleteEngagementUseCase.Param(
                Long.parseLong(engagementId),
                Long.parseLong(memberId),
                userType
        );
    }
}