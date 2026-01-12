package com.lgcns.bebee.payment.domain.repository;

import com.lgcns.bebee.payment.domain.entity.sync.PaymentMatchSync;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<PaymentMatchSync, Long> {
    Optional<PaymentMatchSync> findByMatchId(Long matchId);
}
