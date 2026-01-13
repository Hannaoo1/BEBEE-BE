package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.data.event.payment.HoneyWalletChangedEvent;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.payment.application.usecase.client.EventPublisher;
import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import com.lgcns.bebee.payment.common.exception.PaymentInvalidParamErrors;
import com.lgcns.bebee.payment.domain.entity.HoneyEscrow;
import com.lgcns.bebee.payment.domain.entity.HoneyHistory;
import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import com.lgcns.bebee.payment.domain.entity.sync.PaymentMatchSync;
import com.lgcns.bebee.payment.domain.entity.vo.EscrowStatus;
import com.lgcns.bebee.payment.domain.entity.vo.HoneyHistoryType;
import com.lgcns.bebee.payment.domain.repository.HoneyEscrowRepository;
import com.lgcns.bebee.payment.domain.repository.HoneyHistoryRepository;
import com.lgcns.bebee.payment.domain.repository.HoneyWalletRepository;
import com.lgcns.bebee.payment.domain.service.HoneyEscrowService;
import com.lgcns.bebee.payment.domain.service.HoneyWalletService;
import com.lgcns.bebee.payment.domain.service.MatchReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UseHoneyUseCase implements UseCase<UseHoneyUseCase.Param, Void> {

    private final HoneyWalletService honeyWalletService;
    private final MatchReader matchReader;
    private final HoneyEscrowRepository honeyEscrowRepository;
    private final HoneyEscrowService honeyEscrowService;
    private final HoneyHistoryRepository honeyHistoryRepository;
    private final HoneyWalletRepository honeyWalletRepository;

    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public Void execute(Param param) {
        param.validate();

        // 1. 에스크로(임시 꿀 보관소)에 꿀 임시 보관
        PaymentMatchSync match = matchReader.findById(param.getMatchId());
        // 1-1. 해당 활동에 결제해야 하는 사람이 본인이 맞는지 확인
        if (!match.getDisabledId().equals(param.getDisabledId())) {
            throw PaymentErrors.PAYMENT_MEMBER_MISMATCH.toException();
        }
        // 1-2. 중복 에스크로 체크
        honeyEscrowService.existsByMatchId(match.getMatchId());
        // 1-3. 꿀 → 원 단위로 변환
        Long needHoney = param.getUseHoney();
        Long amount = needHoney * 100;
        // 1-4. 에스크로 생성
        HoneyEscrow escrow = HoneyEscrow.create(
                match,
                match.getDisabledId(),
                match.getHelperId(),
                amount,
                EscrowStatus.PENDING,
                null,
                null
        );
        honeyEscrowRepository.save(escrow);

        // 2. 꿀 차감
        HoneyWallet wallet = honeyWalletService.findByMemberIdWithLock(param.getDisabledId());
        wallet.withdraw(amount);
        HoneyWallet savedWallet = honeyWalletRepository.saveAndFlush(wallet);

        // 3. 꿀 히스토리 기록
        HoneyHistory history = HoneyHistory.create(
                savedWallet,
                param.getDisabledId(),
                amount,
                HoneyHistoryType.WITHDRAWL
        );
        honeyHistoryRepository.save(history);

        // 4. 꿀 차감 후 이벤트 발행
        eventPublisher.publish(new HoneyWalletChangedEvent(
                param.getDisabledId(),
                savedWallet.getHoneyWalletId(),
                savedWallet.getBalance()
        ));

        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long disabledId;
        private final Long matchId;
        private final Long useHoney;

        public boolean validate() {
            if (!ParamValidator.isValidId(disabledId)) {
                throw new InvalidParamException(PaymentInvalidParamErrors.UNAUTHORIZED);
            }
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
