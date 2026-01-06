package com.lgcns.bebee.common.data.event;

public interface EventHandler<T extends DomainEvent> {
    EventType getEventType();
    void handle(T event);
}
