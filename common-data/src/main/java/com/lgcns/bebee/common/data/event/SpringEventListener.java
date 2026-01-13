package com.lgcns.bebee.common.data.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
public class SpringEventListener {
    private final EventPublisher eventPublisher;
    private final OutboxRepository outboxRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(EventEnvelope envelope) {
        Outbox outbox = outboxRepository.findById(envelope.eventId())
                .orElseThrow(() -> new IllegalStateException("Outbox를 찾을 수 없습니다: " + envelope.eventId()));

        try {
            outbox.markAsProceeding();
            eventPublisher.publish(envelope);
            outbox.markAsDone();
            log.info("SNS 발행 성공 - eventId: {}, eventType: {}", envelope.eventId(), envelope.eventType());
        } catch (Exception e) {
            outbox.markAsReady();
            log.error("SNS 발행 실패 - eventId: {}, eventType: {}", envelope.eventId(), envelope.eventType(), e);
            // 실패 시 READY 상태 유지 → OutboxRetryScheduler가 재시도
        }
    }
}
