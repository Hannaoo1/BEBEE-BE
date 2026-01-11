package com.lgcns.bebee.payment.infrastructure.event;


import com.lgcns.bebee.common.data.event.match.AgreementConfirmedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventType;
import com.lgcns.bebee.payment.application.usecase.UseHoneyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgreementConfirmedEventHandler implements EventHandler<AgreementConfirmedEvent> {

    private final UseHoneyUseCase useHoneyUseCase;

    @Override
    public EventType getEventType() {
        return EventType.AGREEMENT_CONFIRMED;
    }

    @Override
    public void handle(AgreementConfirmedEvent event) {
        new UseHoneyUseCase.Param(
                event.getDisabledId(),
                event.getMatchId(),
                event.getUseHoney()
        );
    }
}
