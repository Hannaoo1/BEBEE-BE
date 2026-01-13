package com.lgcns.bebee.common.data.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class EventHandlerRegistry {
    private final List<EventHandler<? extends DomainEvent>> eventHandlers;
    private Map<Class<? extends DomainEvent>, EventHandler<? extends DomainEvent>> handlerMap;

    @PostConstruct
    public void init(){
        handlerMap = eventHandlers.stream()
                .collect(Collectors.toMap(
                        EventHandler::getEventClass,
                        Function.identity()
                ));
    }

    public EventHandler<? extends DomainEvent> getHandler(Class<? extends DomainEvent> eventClass) {
        EventHandler<? extends DomainEvent> handler = handlerMap.get(eventClass);
        if (handler == null) {
            throw new IllegalStateException(String.format("[%s] 해당 이벤트에 대한 핸들러가 없습니다.", eventClass.getSimpleName()));
        }
        return handler;
    }
}
