package com.lgcns.bebee.common.data.event;

public interface EventPublisher {
    void publish(EventEnvelope event);
}
