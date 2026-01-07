package com.lgcns.bebee.payment.domain.repository;

import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HoneyWalletRepository extends JpaRepository<HoneyWallet, Long> {

    Optional<HoneyWallet> findByMemberId(Long memberId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT hw FROM HoneyWallet hw WHERE hw.memberId = :memberId")
    Optional<HoneyWallet> findByMemberIdWithLock(@Param("memberId") Long memberId);
}
