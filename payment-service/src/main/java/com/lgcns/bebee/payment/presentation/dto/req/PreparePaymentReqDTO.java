package com.lgcns.bebee.payment.presentation.dto.req;

import com.lgcns.bebee.payment.application.usecase.PreparePaymentUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "결제 준비 요청 DTO")
public record PreparePaymentReqDTO(
        @Schema(
                description = "회원 ID",
                example = "100",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Long memberId,

        @Schema(
                description = "충전 금액",
                example = "10000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer amount
) {
    public PreparePaymentUseCase.Param toParam() {
        return new PreparePaymentUseCase.Param(memberId, amount);
    }
}