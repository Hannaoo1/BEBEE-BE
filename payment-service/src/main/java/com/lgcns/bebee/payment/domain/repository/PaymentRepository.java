package com.lgcns.bebee.payment.domain.repository;

import com.lgcns.bebee.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
