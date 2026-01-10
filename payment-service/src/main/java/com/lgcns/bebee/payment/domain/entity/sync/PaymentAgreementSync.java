package com.lgcns.bebee.payment.domain.entity.sync;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentAgreementSync extends BaseTimeEntity {
    @Id
    @Column(name = "agreement_id")
    private Long id;

    @Column(nullable = false)
    private Long unitHoney;

    @Column(nullable = false)
    private Long totalHoney;

    @Enumerated(EnumType.STRING)
    private EngagementType type;

    public static PaymentAgreementSync create(Long agreementId, Long unitHoney, Long totalHoney, EngagementType type) {
        PaymentAgreementSync agreement = new PaymentAgreementSync();
        agreement.id = agreementId;
        agreement.unitHoney = unitHoney;
        agreement.totalHoney = totalHoney;
        agreement.type = type;
        return agreement;
    }
}
