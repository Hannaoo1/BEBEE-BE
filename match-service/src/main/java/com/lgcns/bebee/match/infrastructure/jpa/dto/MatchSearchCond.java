package com.lgcns.bebee.match.infrastructure.jpa.dto;

import com.lgcns.bebee.match.domain.entity.vo.EngagementType;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record MatchSearchCond(
        Long memberId,
        LocalDate date,
        DayOfWeek dayOfWeek,
        EngagementType type
) {

    public static MatchSearchCond from(Long memberId, LocalDate date, DayOfWeek dayOfWeek, EngagementType type) {
        return new MatchSearchCond(memberId, date, dayOfWeek, type);
    }
}
