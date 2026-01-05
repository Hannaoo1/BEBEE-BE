package com.lgcns.bebee.payment.presentation.dto.res;

import com.lgcns.bebee.payment.application.usecase.ConfirmPaymentUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "결제 승인 응답 DTO")
public record ConfirmPaymentResDTO(
        @Schema(
                description = "결제 ID",
                example = "1234567890"
        )
        String paymentId,

        @Schema(
                description = "현재 허니 잔액",
                example = "50000"
        )
        Long currentBalance,

        @Schema(
                description = "토스 결제 키",
                example = "tviva20240101000000ABCD1234"
        )
        String paymentKey
) {
    public static ConfirmPaymentResDTO from(ConfirmPaymentUseCase.Result result) {
        return new ConfirmPaymentResDTO(
                result.getPaymentId(),
                result.getCurrentBalance(),
                result.getPaymentKey()
        );
    }
}