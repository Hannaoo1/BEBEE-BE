package com.lgcns.bebee.payment.presentation.dto.res;

import com.lgcns.bebee.payment.application.usecase.GetHoneyBalanceUseCase;

public record HoneyBalanceGetResDTO(
        Long currentHoney
) {
    public static HoneyBalanceGetResDTO from(GetHoneyBalanceUseCase.Result result) {
        return new HoneyBalanceGetResDTO(
                result.getCurrentHoney()
        );
    }
}
