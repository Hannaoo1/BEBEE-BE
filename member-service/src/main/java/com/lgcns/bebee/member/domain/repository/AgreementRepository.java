package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.sync.AgreementSync;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgreementRepository extends JpaRepository<AgreementSync, Long> {

    Optional<AgreementSync> findById(Long agreementId);
}
