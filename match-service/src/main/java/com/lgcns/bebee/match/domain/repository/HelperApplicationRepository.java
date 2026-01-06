package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelperApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByApplicantIdAndPost_Id(Long applicantId, Long postId);
    
    List<Application> findAllByPost_Id(Long postId);
}
