package com.lgcns.bebee.match.infrastructure.jpa;

import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import com.lgcns.bebee.match.infrastructure.jpa.dto.MatchSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryAdapter implements MatchRepository {

    private final JpaMatchRepository matchJpaRepository;
    private final QuerydslMatchRepository querydslMatchRepository;

    @Override
    public Match save(Match match) {
        return matchJpaRepository.save(match);
    }

    @Override
    public Optional<Match> findById(Long matchId) {
        return matchJpaRepository.findById(matchId);
    }

    @Override
    public List<Match> findMatchesByDate(Long memberId, LocalDate date, DayOfWeek dayOfWeek, EngagementType type) {
        MatchSearchCond cond = MatchSearchCond.from(memberId, date, dayOfWeek, type);
        return querydslMatchRepository.searchMatches(cond);
    }

    @Override
    public List<Match> findByMonthAndMember(Long memberId, LocalDate monthStart, LocalDate monthEnd) {
        return matchJpaRepository.findByMonthAndMember(memberId, monthStart, monthEnd);
    }
}