package com.lgcns.bebee.payment.domain.repository;

import com.lgcns.bebee.payment.domain.entity.sync.PaymentMatchSync;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<PaymentMatchSync, Integer> {
    PaymentMatchSync findById(Long matchId);
}
