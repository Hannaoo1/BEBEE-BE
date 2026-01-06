package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import com.lgcns.bebee.payment.common.exception.PaymentInvalidParamErrors;
import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import com.lgcns.bebee.payment.domain.repository.HoneyWalletRepository;
import com.lgcns.bebee.payment.domain.service.HoneyWalletService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UseHoneyUseCase implements UseCase<UseHoneyUseCase.Param, Void> {

    private final HoneyWalletRepository xhoneyWalletRepository;
    private final HoneyWalletService honeyWalletService;

    @Override
    @Transactional
    public Void execute(Param param) {
        param.validate();
        // 현재 꿀 개수가 충분하면 바로 차감, 충분하지 않으면 충전으로 넘어가야 함(예외 던지고, 버튼은 충전하기도 항상 있는걸로)
        Long currentHoney = honeyWalletService.getCurrentHoney(param.getMemberId());
        Long needHoney = param.getUseHoney();
        if (currentHoney < needHoney) {
            throw PaymentErrors.INSUFFICIENT_HONEY_BALANCE.toException();
        }
        HoneyWallet wallet = honeyWalletService.findByMemberId(param.getMemberId());
        wallet.withdraw(needHoney);

        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private Long memberId;
        private final Long useHoney; // 나눔일 때는 api 요청 자체를 x

        public boolean validate() {
            if (!ParamValidator.isNotNull(useHoney)) {
                throw new InvalidParamException(PaymentInvalidParamErrors.REQUIRED_FIELD, "useHoney");
            }
            if (!ParamValidator.isPositiveNumber(useHoney)) {
                throw new InvalidParamException(PaymentInvalidParamErrors.HONEY_USE_MUST_BE_POSITIVE);
            }

            return true;
        }
    }
}
