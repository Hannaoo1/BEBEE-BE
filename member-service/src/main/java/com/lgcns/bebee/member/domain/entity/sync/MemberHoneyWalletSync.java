package com.lgcns.bebee.member.domain.entity.sync;

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
public class MemberHoneyWalletSync extends BaseTimeEntity {
    @Id
    private Long honeyWalletId;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Column(nullable = false)
    private Long balance = 0L; // 잔액 (원 단위)

    public static MemberHoneyWalletSync create(Long honeyWalletId, Long memberId, Long balance) {
        MemberHoneyWalletSync honeyWallet = new MemberHoneyWalletSync();
        honeyWallet.honeyWalletId = honeyWalletId;
        honeyWallet.memberId = memberId;
        honeyWallet.balance = balance;

        return honeyWallet;
    }

    public void updateBalance(Long balance) {
        this.balance = balance;
    }
}
