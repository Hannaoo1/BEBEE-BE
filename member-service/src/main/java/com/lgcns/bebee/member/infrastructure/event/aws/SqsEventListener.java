package com.lgcns.bebee.member.infrastructure.event.aws;

// TODO: 이벤트 처리 활성화 예정
// SQS 의존성 비활성화로 인해 주석 처리

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lgcns.bebee.common.data.event.DomainEvent;
//import com.lgcns.bebee.common.data.event.EventHandler;
//import com.lgcns.bebee.common.data.event.EventType;
//import com.lgcns.bebee.member.infrastructure.event.EventHandlerRegistry;
//import io.awspring.cloud.sqs.annotation.SqsListener;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@ConditionalOnProperty(name = "event", havingValue = "aws")
//@RequiredArgsConstructor
//public class SqsEventListener {
//    private final ObjectMapper objectMapper;
//    private final EventHandlerRegistry handlerRegistry;
//
//    @SqsListener("${app.sqs.member-queue-url}")
//    public void handleEvent(@Payload String payload, @Header("eventType") String eventType) {
//        try{
//            log.info("SQS 이벤트 수신 - 타입: {}, 페이로드: {}", eventType, payload);
//
//            EventType type = EventType.from(eventType);
//            DomainEvent event = objectMapper.readValue(payload, type.getEventClass());
//
//            processEvent(event, type);
//        } catch (JsonProcessingException e) {
//            log.error("SQS 메시지 처리 실패: {}", payload, e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private <T extends DomainEvent> void processEvent(DomainEvent event, EventType type) {
//        EventHandler<T> handler = (EventHandler<T>) handlerRegistry.getHandler(type);
//        handler.handle((T) event);
//    }
//}
