package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface EngagementRepository extends JpaRepository<Engagement, Long> {

    // 특정 날짜의 특정 상태 활동 조회 (스케줄러)
    List<Engagement> findByActivityDateAndStatus(
            LocalDate activityDate,
            EngagementStatus status
    );
}
