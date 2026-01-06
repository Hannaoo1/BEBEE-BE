package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.vo.AgreementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    List<Agreement> findByPostId(Long postId);

    Boolean existsByPostIdAndStatus(Long postId, AgreementStatus status);
}
