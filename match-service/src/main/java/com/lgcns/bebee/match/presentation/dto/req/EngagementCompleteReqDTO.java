package com.lgcns.bebee.match.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "활동 완료 체크 요청")
@Getter
@NoArgsConstructor
public class EngagementCompleteReqDTO {

    @Schema(description = "사용자 유형 (HELPER: 도우미, DISABLED: 장애인)",
            example = "HELPER",
            allowableValues = {"HELPER", "DISABLED"})
    @NotNull(message = "사용자 유형은 필수입니다")
    private String userType;
}
