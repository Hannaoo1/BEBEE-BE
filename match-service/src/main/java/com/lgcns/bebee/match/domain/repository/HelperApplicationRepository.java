package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelperApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByApplicantIdAndPost_Id(Long applicantId, Long postId);
}
