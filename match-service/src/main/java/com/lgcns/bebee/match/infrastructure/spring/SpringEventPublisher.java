package com.lgcns.bebee.match.infrastructure.spring;

import com.lgcns.bebee.common.data.event.DomainEvent;
import com.lgcns.bebee.match.application.usecase.client.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "event.type", havingValue = "spring")
@RequiredArgsConstructor
public class SpringEventPublisher implements EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publish(DomainEvent event) {
        log.info("{}: {} 발행, 발행 시간 = {}", event.getEventName(), event.getEventDesc(), event.getOccuredAt());

        eventPublisher.publishEvent(event);
    }
}
