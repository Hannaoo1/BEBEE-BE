package com.lgcns.bebee.payment.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoneyWallet extends BaseTimeEntity {
    @Id
    @Tsid
    private Long honeyWalletId;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Column(nullable = false)
    private Long balance = 0L;

    public static HoneyWallet create(Long memberId, Long balance) {
        HoneyWallet honeyWallet = new HoneyWallet();
        honeyWallet.memberId = memberId;
        honeyWallet.balance = balance;

        return honeyWallet;
    }

    /**
     * 허니 충전 (장애인: 결제 완료 시 / 도우미: 꿀 지급 완료 시)
     */
    public void charge(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        this.balance += amount;
    }

    /**
     * 허니 차감 (결제 취소 또는 사용 시)
     */
    public void withdraw(Long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("차감 금액은 0보다 커야 합니다.");
        }
        if (this.balance < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        this.balance -= amount;
    }
}
