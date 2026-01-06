package com.lgcns.bebee.common.data.domain;

import java.time.LocalDateTime;

public abstract class DomainEvent {
    private LocalDateTime occuredAt;

    public LocalDateTime getOccuredAt(){
        return LocalDateTime.now();
    }

    public abstract String getEventName();
    public abstract String getEventDesc();
}
