package com.lgcns.bebee.common.data.event;

import java.time.LocalDateTime;

public record EventEnvelope(
        Long eventId,
        String eventType,
        DomainEvent event,
        LocalDateTime producedAt
) {
    public static EventEnvelope from(Long eventId, DomainEvent event){
        return new EventEnvelope(
                eventId,
                event.getClass().getSimpleName(),
                event,
                LocalDateTime.now()
        );
    }
}
