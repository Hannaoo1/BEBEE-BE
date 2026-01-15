package com.lgcns.bebee.payment.infrastructure.event;

import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.match.EngagementCompletedEvent;
import com.lgcns.bebee.payment.application.usecase.GrantHelperHoneyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EngagementCompletedEventHandler implements EventHandler<EngagementCompletedEvent> {
    private final GrantHelperHoneyUseCase grantHelperHoneyUseCase;

    @Override
    public Class<EngagementCompletedEvent> getEventClass() {
        return EngagementCompletedEvent.class;
    }

    @Override
    public void handle(EngagementCompletedEvent event) {
        GrantHelperHoneyUseCase.Param param = new GrantHelperHoneyUseCase.Param(
                event.getMatchId()
        );

        grantHelperHoneyUseCase.execute(param);
    }
}
