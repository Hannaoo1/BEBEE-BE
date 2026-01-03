package com.lgcns.bebee.match.domain.service;

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

    // Agreement 확정 시 Engagement 생성 (DAY)
    @Transactional
    public Engagement createEngagement(Agreement agreement) {
            LocalDate activityDate = agreement.getPeriod().getStartDate();

            Engagement engagement = Engagement.create(
                    agreement.getId(),
                    EngagementType.DAY,
                    activityDate
            );

            // DB 저장
            return engagementRepository.save(engagement);
    }
}
