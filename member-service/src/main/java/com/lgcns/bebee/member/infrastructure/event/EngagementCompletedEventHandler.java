package com.lgcns.bebee.member.infrastructure.event;

import com.lgcns.bebee.common.data.event.match.EngagementCompletedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.member.application.usecase.CreateBadgeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EngagementCompletedEventHandler implements EventHandler<EngagementCompletedEvent> {
    private final CreateBadgeUseCase createBadgeUseCase;

    @Override
    public Class<EngagementCompletedEvent> getEventClass() {
        return EngagementCompletedEvent.class;
    }

    @Override
    public void handle(EngagementCompletedEvent event) {
        CreateBadgeUseCase.Param param = new CreateBadgeUseCase.Param(event.getAgreementId());
        createBadgeUseCase.execute(param);
    }
}
