package com.lgcns.bebee.match.infrastructure.event.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.bebee.common.data.event.DomainEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventType;
import com.lgcns.bebee.match.infrastructure.event.EventHandlerRegistry;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "event", havingValue = "aws")
@RequiredArgsConstructor
public class SqsEventListener {
    private final ObjectMapper objectMapper;
    private final EventHandlerRegistry handlerRegistry;

    @SqsListener("${app.sqs.match-queue-url}")
    public void handleEvent(String message) {
        try{
            log.info("SQS 메시지 수신: {}", message);

            JsonNode root = objectMapper.readTree(message);

            String snsType = root.path("Type").asText();
            if (!"Notification".equals(snsType)) {
                log.info("SNS Notification 아님. 무시합니다. type={}", snsType);
                return;
            }

            JsonNode attributes = root.get("MessageAttributes");
            String eventType = attributes.get("eventType").get("Value").asText();

            if (eventType.isEmpty()) {
                log.warn("eventType 없음. 메시지 무시");
                return;
            }

            String payload = root.get("Message").asText();

            log.info("이벤트 타입: {}, 페이로드: {}", eventType, payload);

            EventType type = EventType.from(eventType);
            DomainEvent event = objectMapper.readValue(payload, type.getEventClass());

            processEvent(event, type);
        } catch (JsonProcessingException e) {
            log.error("SQS 메시지 처리 실패: {}", message, e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends DomainEvent> void processEvent(DomainEvent event, EventType type) {
        EventHandler<T> handler = (EventHandler<T>) handlerRegistry.getHandler(type);
        handler.handle((T) event);
    }
}
