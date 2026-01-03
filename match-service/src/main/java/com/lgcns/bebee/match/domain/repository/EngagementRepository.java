package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface EngagementRepository extends JpaRepository<Engagement, Long> {

    // PENDING 상태이면서 activityDate가 기준일 이하(3일이 지난)인 활동 목록
    List<Engagement> findByStatusAndActivityDateLessThanEqual(
            EngagementStatus status,
            LocalDate activityDate
    );
}
