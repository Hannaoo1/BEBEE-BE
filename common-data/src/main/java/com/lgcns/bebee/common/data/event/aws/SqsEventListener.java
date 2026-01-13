package com.lgcns.bebee.common.data.event.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.bebee.common.data.event.DomainEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventHandlerRegistry;
import com.lgcns.bebee.common.data.event.EventTypeMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
@RequiredArgsConstructor
public class SqsEventListener {
    private final ObjectMapper objectMapper;
    private final EventTypeMapper eventTypeMapper;
    private final EventHandlerRegistry handlerRegistry;

    @SqsListener("${app.sqs.queue-url}")
    public void handleEvent(@Payload String payload, @Header("eventType") String eventType) {
        Class<? extends DomainEvent> eventClass = eventTypeMapper.getClass(eventType);
        DomainEvent event = parseEvent(eventClass, payload);

        log.info("SQS 이벤트 수신 - 타입: {}", eventType);

        processEvent(event, eventClass);
    }

    private DomainEvent parseEvent(Class<? extends DomainEvent> eventClass, String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            JsonNode eventNode = root.get("event");
            return objectMapper.treeToValue(eventNode, eventClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("이벤트 페이로드 역직렬화 중 오류 발생", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends DomainEvent> void processEvent(DomainEvent event, Class<? extends DomainEvent> eventClass) {
        EventHandler<T> handler = (EventHandler<T>) handlerRegistry.getHandler(eventClass);
        handler.handle((T) event);
    }
}
