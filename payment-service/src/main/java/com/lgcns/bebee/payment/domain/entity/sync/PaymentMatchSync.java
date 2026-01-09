package com.lgcns.bebee.payment.domain.entity.sync;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentMatchSync extends BaseTimeEntity {
    @Id
    private Long matchId;

    @Column(nullable = false)
    private Long helperId;

    @Column(nullable = false)
    private Long disabledId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false, unique = true)
    private PaymentAgreementSync agreement;

    public static PaymentMatchSync create(Long matchId, Long helperId, Long disabledId, PaymentAgreementSync agreement) {
        PaymentMatchSync match = new PaymentMatchSync();
        match.matchId = matchId;
        match.helperId = helperId;
        match.disabledId = disabledId;
        match.agreement = agreement;
        return match;
    }
}
