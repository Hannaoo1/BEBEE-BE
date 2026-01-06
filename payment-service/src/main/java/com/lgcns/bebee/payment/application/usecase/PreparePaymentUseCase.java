package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.payment.application.client.TempPaymentPort;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreparePaymentUseCase implements UseCase<PreparePaymentUseCase.Param, PreparePaymentUseCase.Result> {

    private final TempPaymentPort tempPaymentPort;

    @Override
    public Result execute(Param param) {
        String orderId = tempPaymentPort.save(
                param.getMemberId(),
                param.getAmount()
        );

        return new Result(orderId, param.getAmount());
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final Long amount;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private final String orderId;
        private final Long amount;
    }
}