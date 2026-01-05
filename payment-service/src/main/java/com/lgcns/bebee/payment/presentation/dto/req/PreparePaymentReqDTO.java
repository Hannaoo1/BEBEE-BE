package com.lgcns.bebee.payment.presentation.dto.req;

import com.lgcns.bebee.payment.application.usecase.PreparePaymentUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "결제 준비 요청 DTO")
public record PreparePaymentReqDTO(
        @Schema(
                description = "충전 금액",
                example = "10000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer amount
) {
    public PreparePaymentUseCase.Param toParam(Long memberId) {
        return new PreparePaymentUseCase.Param(memberId, amount);
    }
}