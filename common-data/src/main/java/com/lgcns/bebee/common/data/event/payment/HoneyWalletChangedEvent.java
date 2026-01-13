package com.lgcns.bebee.common.data.event.payment;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 결제 승인 이벤트 (HoneyWallet 생성/충전/차감 시 발행)
 *
 * 발행 시점:
 * - 결제 완료 후 HoneyWallet 충전 (ConfirmPaymentUseCase)
 * - Agreement 확정 시 HoneyWallet 차감 (UseHoneyUseCase)
 *
 * 처리:
 * - member-service: member_honey_wallet_sync 테이블 동기화
 */
@Getter
@RequiredArgsConstructor
public class HoneyWalletChangedEvent extends DomainEvent {
    private final Long memberId;
    private final Long honeyWalletId;
    private final Long balance; // 변경 후 잔액 (원 단위)

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getEventDesc() {
        return "결제 승인 이벤트";
    }
}
