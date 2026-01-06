package com.lgcns.bebee.match.infrastructure.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.bebee.common.data.event.DomainEvent;
import com.lgcns.bebee.match.application.usecase.client.EventPublisher;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "event.type", havingValue = "aws")
@RequiredArgsConstructor
public class SnsEventPublisher implements EventPublisher {
    private final SnsTemplate snsTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.sns.match-topic-arn}")
    private String topicArn;

    @Override
    public void publish(DomainEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);

            log.info("SNS 발행 시작 - Topic: {}, Event: {}", topicArn, event.getEventName());

            snsTemplate.sendNotification(topicArn, message, event.getEventName());

            log.info("SNS 발행 완료 - Event: {}, 발행 시간: {}", event.getEventName(), event.getOccuredAt());
        } catch (JsonProcessingException e) {
            log.error("SNS 이벤트 발행 실패 - Event: {}", event.getEventName(), e);
            throw new RuntimeException("SNS 이벤트 발행 실패", e);
        }
    }
}
