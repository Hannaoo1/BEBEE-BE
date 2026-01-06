package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    Match save(Match match);

    Optional<Match> findById(Long postId);

    /**
     * 특정 날짜에 활동하는 매칭 목록 조회
     */
    List<Match> findByDateAndMember(Long memberId, LocalDate date, DayOfWeek dayOfWeek, EngagementType type);

    /**
     * 특정 월에 활동하는 매칭 목록 조회 (캘린더용)
     */
    List<Match> findByMonthAndMember(Long memberId, LocalDate monthStart, LocalDate monthEnd);

    // Agreement ID를 통해 Match 조회
    Optional<Match> findByAgreementId(Long agreementId);
}
