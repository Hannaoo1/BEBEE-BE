package com.lgcns.bebee.match.infrastructure.event.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.bebee.common.data.event.DomainEvent;
import com.lgcns.bebee.match.application.usecase.client.EventPublisher;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.Map;

@Slf4j
@Component
@ConditionalOnProperty(name = "event", havingValue = "aws")
@RequiredArgsConstructor
public class SnsEventPublisher implements EventPublisher {
    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @Value("${app.sns.match-topic-arn}")
    private String topicArn;

    @Override
    public void publish(DomainEvent event) {
        try {
            String messagePayload = objectMapper.writeValueAsString(event);

            log.info("SNS 발행 시작 - Topic: {}, Event: {}", topicArn, event.getEventName());

            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(messagePayload)
                    .messageAttributes(Map.of(
                            "eventType", MessageAttributeValue.builder()
                                    .dataType("String")
                                    .stringValue(event.getEventName())
                                    .build()
                    ))
                    .build();

            snsClient.publish(request);

            log.info("SNS 발행 완료 - Event: {}, 발행 시간: {}", event.getEventName(), event.getOccuredAt());
        } catch (JsonProcessingException e) {
            log.error("SNS 이벤트 발행 실패 - Event: {}", event.getEventName(), e);
            throw new RuntimeException("SNS 이벤트 발행 실패", e);
        }
    }
}
