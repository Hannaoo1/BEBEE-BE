package com.lgcns.bebee.payment.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.bebee.common.data.event.DomainEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventHandlerRegistry;
import com.lgcns.bebee.common.data.event.EventTypeMapper;
import com.lgcns.bebee.common.data.event.aws.SqsEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EventConfig {
    @Bean
    public EventHandlerRegistry eventHandlerRegistry(List<EventHandler<? extends DomainEvent>> handlers){
        return new EventHandlerRegistry(handlers);
    }

    @Bean
    public SqsEventListener sqsEventListener(
            EventTypeMapper eventTypeMapper,
            EventHandlerRegistry eventHandlerRegistry,
            ObjectMapper objectMapper){
        return new SqsEventListener(objectMapper, eventTypeMapper, eventHandlerRegistry);
    }
}
