package com.lgcns.bebee.payment.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import com.lgcns.bebee.payment.domain.entity.vo.PaymentStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {
    @Id
    @Tsid
    private Long paymentId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 200, unique = true)
    private String paymentKey;

    @Column(nullable = false, length = 64, unique = true)
    private String orderId;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    public static Payment create(Long memberId, String paymentKey, String orderId, Integer amount) {
        Payment payment = new Payment();
        payment.memberId = memberId;
        payment.paymentKey = paymentKey;
        payment.orderId = orderId;
        payment.amount = amount;
        payment.status = PaymentStatus.PENDING;  // 초기 상태

        return payment;
    }

    public void markAsPaid() {
        this.status = PaymentStatus.PAID;
    }

    public void markAsCanceled() {
        this.status = PaymentStatus.CANCELED;
    }

    public void validateOwner(Long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new IllegalArgumentException("결제 소유자가 아닙니다.");
        }
    }
}
