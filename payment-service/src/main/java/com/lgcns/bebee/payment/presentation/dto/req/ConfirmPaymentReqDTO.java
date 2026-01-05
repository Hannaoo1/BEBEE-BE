package com.lgcns.bebee.payment.presentation.dto.req;

import com.lgcns.bebee.payment.application.usecase.ConfirmPaymentUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "결제 승인 요청 DTO")
public record ConfirmPaymentReqDTO(
        @Schema(
                description = "토스 결제 키 (토스 위젯에서 받은 값)",
                example = "tviva20240101000000ABCD1234",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String paymentKey,

        @Schema(
                description = "주문 ID (결제 준비 시 받은 orderId)",
                example = "0HQKZ2Z3Z4Z5Z6Z7Z8Z9ZA",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String orderId,

        @Schema(
                description = "결제 금액",
                example = "10000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer amount
) {
    public ConfirmPaymentUseCase.Param toParam(Long memberId) {
        return new ConfirmPaymentUseCase.Param(orderId, paymentKey, amount, memberId);
    }
}