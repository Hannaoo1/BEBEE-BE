package com.lgcns.bebee.payment.presentation.dto.res;

import com.lgcns.bebee.payment.application.usecase.PreparePaymentUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "결제 준비 응답 DTO")
public record PreparePaymentResDTO(
        @Schema(
                description = "주문 ID",
                example = "0HQKZ2Z3Z4Z5Z6Z7Z8Z9ZA"
        )
        String orderId,

        @Schema(
                description = "충전 금액",
                example = "10000"
        )
        Integer amount
) {
    public static PreparePaymentResDTO from(PreparePaymentUseCase.Result result) {
        return new PreparePaymentResDTO(
                result.getOrderId(),
                result.getAmount()
        );
    }
}