package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    
    // Agreement ID를 통해 Match 조회
    Optional<Match> findByAgreementId(Long agreementId);
}
