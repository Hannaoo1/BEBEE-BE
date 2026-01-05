package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.payment.application.client.TossPaymentsClient;
import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import com.lgcns.bebee.payment.domain.entity.HoneyHistory;
import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import com.lgcns.bebee.payment.domain.entity.Payment;
import com.lgcns.bebee.payment.domain.entity.vo.HoneyHistoryType;
import com.lgcns.bebee.payment.domain.repository.HoneyHistoryRepository;
import com.lgcns.bebee.payment.domain.repository.HoneyWalletRepository;
import com.lgcns.bebee.payment.domain.repository.PaymentRepository;
import com.lgcns.bebee.payment.infrastructure.redis.RedisTempPaymentService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmPaymentUseCase implements UseCase<ConfirmPaymentUseCase.Param, ConfirmPaymentUseCase.Result> {

    private final RedisTempPaymentService redisTempPaymentService;
    private final TossPaymentsClient tossPaymentsClient;
    private final PaymentRepository paymentRepository;
    private final HoneyWalletRepository honeyWalletRepository;
    private final HoneyHistoryRepository honeyHistoryRepository;

    @Transactional
    @Override
    public Result execute(Param param) {
        // Redis에 이전에 저장한 임시 결제 정보 조회 + 값이 일치하는지 검증
        RedisTempPaymentService.TempPaymentDto temp = redisTempPaymentService.get(param.getOrderId());

        if (!temp.getAmount().equals(param.getAmount())) {
            log.error("결제 금액 불일치: Redis={}, Param={}", temp.getAmount(), param.getAmount());
            throw PaymentErrors.PAYMENT_AMOUNT_MISMATCH.toException();
        }

        if (!temp.getMemberId().equals(param.getMemberId())) {
            log.error("회원 ID 불일치: Redis={}, Param={}", temp.getMemberId(), param.getMemberId());
            throw PaymentErrors.PAYMENT_MEMBER_MISMATCH.toException();
        }

        // 토스 승인 API 호출 (TossPaymentsClient 사용)
        TossPaymentsClient.TossPaymentResponse response = tossPaymentsClient.confirmPayment(
                param.getPaymentKey(),
                param.getOrderId(),
                param.getAmount()
        );

        // 결제 완료 처리 (Payment 객체 생성)
        Payment payment = Payment.create(
                temp.getMemberId(),
                response.paymentKey(),
                response.orderId(),
                response.totalAmount()
        );
        payment.markAsPaid();  // 상태를 PAID로 변경
        Payment savedPayment = paymentRepository.save(payment);
        log.info("결제 저장 완료: paymentId={}", savedPayment.getPaymentId());

        // HoneyWallet 충전 (비관적 락 적용)
        HoneyWallet wallet = honeyWalletRepository.findByMemberIdWithLock(temp.getMemberId())
                .orElseGet(() -> HoneyWallet.create(temp.getMemberId(), 0L));
        wallet.charge(response.totalAmount());
        honeyWalletRepository.save(wallet);
        log.info("허니 충전 완료: memberId={}, amount={}, balance={}",
                temp.getMemberId(), response.totalAmount(), wallet.getBalance());

        // HoneyHistory 기록
        HoneyHistory history = HoneyHistory.create(
                wallet,
                temp.getMemberId(),
                response.totalAmount(),
                HoneyHistoryType.CHARGE
        );
        honeyHistoryRepository.save(history);
        log.info("허니 히스토리 기록 완료: historyId={}", history.getHoneyHistoryId());

        // 6. Redis 임시 데이터 삭제
        redisTempPaymentService.delete(param.getOrderId());
        log.info("Redis 임시 데이터 삭제 완료: orderId={}", param.getOrderId());

        return new Result(
                String.valueOf(savedPayment.getPaymentId()),
                wallet.getBalance().intValue(),
                response.paymentKey()
        );
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final String orderId;
        private final String paymentKey;
        private final Integer amount;
        private final Long memberId;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private final String paymentId;
        private final Integer currentBalance;
        private final String paymentKey;
    }
}