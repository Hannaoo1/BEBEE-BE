package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.repository.EngagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EngagementReader {
    private final EngagementRepository engagementRepository;

    @Transactional(readOnly = true)
    public Engagement getById(Long engagementId) {
        return engagementRepository.findById(engagementId)
                .orElseThrow(MatchErrors.ENGAGEMENT_NOT_FOUND::toException);
    }

    @Transactional(readOnly = true)
    public Optional<Engagement> findByAgreementId(Long agreementId) {
        return engagementRepository.findByAgreementId(agreementId);
    }
}
