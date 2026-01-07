package com.lgcns.bebee.payment.domain.repository;

import com.lgcns.bebee.payment.domain.entity.HoneyEscrow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoneyEscrowRepository extends JpaRepository<HoneyEscrow, Long> {
    boolean existsByMatchId(Long matchId);
}
