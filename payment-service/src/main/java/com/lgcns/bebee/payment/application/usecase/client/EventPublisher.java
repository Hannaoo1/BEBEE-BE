package com.lgcns.bebee.payment.application.usecase.client;

import com.lgcns.bebee.common.data.event.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
