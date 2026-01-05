package com.lgcns.bebee.match.application.usecase.client;

import com.lgcns.bebee.common.data.domain.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
