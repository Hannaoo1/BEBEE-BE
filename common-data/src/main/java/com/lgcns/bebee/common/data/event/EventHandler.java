package com.lgcns.bebee.common.data.event;

public interface EventHandler<T extends DomainEvent> {
    Class<T> getEventClass();
    void handle(T event);
}
