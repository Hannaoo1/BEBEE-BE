package com.lgcns.bebee.match.infrastructure.event;

import com.lgcns.bebee.common.data.event.EngagementCompletedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventType;
import com.lgcns.bebee.match.application.usecase.CreateBadgeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        CreateBadgeUseCase.Param param = new CreateBadgeUseCase.Param(event.getAgreementId());
        createBadgeUseCase.execute(param);
    }
}
