package com.lgcns.bebee.match.infrastructure.jpa;

import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryAdapter implements MatchRepository {

    private final MatchJpaRepository matchJpaRepository;

    @Override
    public Match save(Match match) {
        return matchJpaRepository.save(match);
    }

    @Override
    public Optional<Match> findById(Long postId) {
        return matchJpaRepository.findByPostId(postId);
    }

    @Override
    public List<Match> findByDateAndMember(Long memberId, LocalDate date, DayOfWeek dayOfWeek, EngagementType type) {
        return matchJpaRepository.findByDateAndMember(memberId, date, dayOfWeek, type);
    }

    @Override
    public List<Match> findByMonthAndMember(Long memberId, LocalDate monthStart, LocalDate monthEnd) {
        return matchJpaRepository.findByMonthAndMember(memberId, monthStart, monthEnd);
    }
}