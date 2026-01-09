package com.lgcns.bebee.match.domain.repository.dto;

import com.lgcns.bebee.match.domain.entity.vo.EngagementType;

import java.time.LocalDate;

public record EngagementSearchCond(
        EngagementType type,
        LocalDate date
) {

    public static EngagementSearchCond from(EngagementType type, LocalDate date) {
        return new EngagementSearchCond(type, date);
    }
}
