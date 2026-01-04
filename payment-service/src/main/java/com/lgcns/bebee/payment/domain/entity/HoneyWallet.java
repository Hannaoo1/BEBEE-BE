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

//    public void charge(Integer amount) {}
//
//    public void withdraw(Integer amount) {}
}
