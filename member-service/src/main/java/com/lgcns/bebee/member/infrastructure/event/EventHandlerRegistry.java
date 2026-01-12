package com.lgcns.bebee.member.infrastructure.event;

import com.lgcns.bebee.common.data.event.DomainEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandlerRegistry {
    private final List<EventHandler<? extends DomainEvent>> eventHandlers;
    private Map<EventType, EventHandler<? extends DomainEvent>> handlerMap;

    @PostConstruct
    public void init(){
        handlerMap = eventHandlers.stream()
                .collect(Collectors.toMap(
                        EventHandler::getEventType,
                        Function.identity()
                ));
    }

    public EventHandler<? extends DomainEvent> getHandler(EventType type) {
        EventHandler<? extends DomainEvent> handler = handlerMap.get(type);
        if (handler == null) {
            throw new IllegalStateException(String.format("[%s] 해당 이벤트에 대한 핸들러가 없습니다.", type.name()));
        }
        return handler;
    }
}
