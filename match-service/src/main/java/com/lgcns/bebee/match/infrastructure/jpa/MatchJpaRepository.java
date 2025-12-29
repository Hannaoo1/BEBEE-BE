package com.lgcns.bebee.match.infrastructure.jpa;

import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MatchJpaRepository extends JpaRepository<Match, Long> {

    /**
     * postId로 매칭 조회
     */
    Optional<Match> findByPostId(Long postId);

    /**
     * 특정 날짜에 활동하는 매칭 목록 조회
     * - DAY: 활동 날짜가 해당 날짜와 일치
     * - TERM: 활동 기간 내에 있고, 해당 요일에 활동하는 매칭
     */
    @Query("""
        SELECT m FROM Match m
        JOIN FETCH m.agreement a
        JOIN FETCH a.period p
        WHERE (m.helperId = :memberId OR m.disabledId = :memberId)
        AND (:type IS NULL OR a.type = :type)
        AND (
            (a.type = 'DAY' AND p.startDate = :date)
            OR
            (a.type = 'TERM'
             AND p.startDate <= :date
             AND :date <= p.endDate
             AND EXISTS (
                 SELECT 1 FROM AgreementSchedule s2
                 WHERE s2.agreement = a
                 AND s2.dayOfWeek = :dayOfWeek
             ))
        )
        """)
    List<Match> findByDateAndMember(
            @Param("memberId") Long memberId,
            @Param("date") LocalDate date,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("type") EngagementType type
    );

    /**
     * 특정 월에 활동하는 매칭 목록 조회 (캘린더용)
     * - 해당 월과 기간이 겹치는 모든 매칭
     */
    @Query("""
        SELECT m FROM Match m
        JOIN FETCH m.agreement a
        JOIN FETCH a.period p
        WHERE (m.helperId = :memberId OR m.disabledId = :memberId)
        AND (p.startDate <= :monthEnd AND p.endDate >= :monthStart)
        """)
    List<Match> findByMonthAndMember(
            @Param("memberId") Long memberId,
            @Param("monthStart") LocalDate monthStart,
            @Param("monthEnd") LocalDate monthEnd
    );
}
