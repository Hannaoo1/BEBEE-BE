package com.lgcns.bebee.match.infrastructure.event;

import com.lgcns.bebee.common.data.event.match.AgreementConfirmedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventType;
import com.lgcns.bebee.match.application.usecase.CreateAgreementEngagementsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgreementConfirmedEventHandler implements EventHandler<AgreementConfirmedEvent> {
    private final CreateAgreementEngagementsUseCase createAgreementEngagementsUseCase;

    @Override
    public EventType getEventType() {
        return  EventType.AGREEMENT_CONFIRMED;
    }

    @Override
    public void handle(AgreementConfirmedEvent event) {
        CreateAgreementEngagementsUseCase.Param param = new CreateAgreementEngagementsUseCase.Param(event.getMatchId());
        createAgreementEngagementsUseCase.execute(param);
    }
}
