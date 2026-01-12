package com.lgcns.bebee.member.infrastructure.event;

import com.lgcns.bebee.common.data.event.EngagementCompletedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventType;
import com.lgcns.bebee.member.application.usecase.CreateBadgeUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EngagementCompletedEventHandler implements EventHandler<EngagementCompletedEvent> {
    private final CreateBadgeUseCase createBadgeUseCase;

    @Override
    public EventType getEventType() {
        return EventType.ENGAGEMENT_COMPLETED;
    }

    @Override
    public void handle(EngagementCompletedEvent event) {
        log.info("활동 완료 이벤트 처리 시작 - agreementId: {}", event.getAgreementId());
        CreateBadgeUseCase.Param param = new CreateBadgeUseCase.Param(event.getAgreementId());
        createBadgeUseCase.execute(param);
        log.info("활동 완료 이벤트 처리 완료 - agreementId: {}", event.getAgreementId());
    }
}
