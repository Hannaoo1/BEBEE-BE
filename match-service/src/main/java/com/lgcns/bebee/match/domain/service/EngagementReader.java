package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.repository.EngagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EngagementReader {
    private final EngagementRepository engagementRepository;

    // 활동 조회
    @Transactional(readOnly = true)
    public Engagement getById(Long engagementId) {
        return engagementRepository.findById(engagementId)
                .orElseThrow(MatchErrors.ENGAGEMENT_NOT_FOUND::toException);
    }
}
