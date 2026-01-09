package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.EngagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EngagementManager {
    private final EngagementRepository engagementRepository;

    @Transactional(readOnly = true)
    public Engagement findExistingEngagement(Long engagementId){
        return engagementRepository.findById(engagementId).orElseThrow(MatchErrors.ENGAGEMENT_NOT_FOUND::toException);
    }
}
