package com.lgcns.bebee.match.domain.repository.dto;

import com.lgcns.bebee.match.domain.entity.vo.EngagementType;

import java.time.LocalDate;

public record EngagementSearchCond(
        Long memberId,
        EngagementType type,
        LocalDate date
) {

    public static EngagementSearchCond from(Long memberId, EngagementType type, LocalDate date) {
        return new EngagementSearchCond(memberId, type, date);
    }
}
