package com.lgcns.bebee.common.data.event.match;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReviewCreatedEvent implements DomainEvent {
    private final Long reviewId;
    private final Long matchId;
    private final Long reviewerId;
    private final Long revieweeId;
    private final String reviewDirection;
    private final List<Integer> keywordIds;
}
