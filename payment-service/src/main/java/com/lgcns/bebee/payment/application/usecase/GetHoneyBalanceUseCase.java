package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import com.lgcns.bebee.payment.domain.repository.HoneyWalletRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetHoneyBalanceUseCase implements UseCase<GetHoneyBalanceUseCase.Param, GetHoneyBalanceUseCase.Result> {

    private final HoneyWalletRepository honeyWalletRepository;

    @Override
    @Transactional
    public Result execute(Param param) {
        Optional<HoneyWallet> wallet = honeyWalletRepository.findByMemberId(param.getMemberId());
        Long balance = wallet.map(HoneyWallet::getBalance).orElse(0L);
        Long currentHoney = balance / 100;

        return Result.from(currentHoney);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private Long currentHoney;

        public static  Result from(Long currentHoney) {
            return new Result(currentHoney);
        }
    }
}
