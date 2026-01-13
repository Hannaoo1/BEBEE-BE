package com.lgcns.bebee.chat.infrastructure.event;

import com.lgcns.bebee.chat.application.ProcessAgreementConfirmedUseCase;
import com.lgcns.bebee.common.data.event.match.AgreementConfirmedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgreementConfirmedEventHandler implements EventHandler<AgreementConfirmedEvent> {
    private final ProcessAgreementConfirmedUseCase processAgreementConfirmedUseCase;

    @Override
    public Class<AgreementConfirmedEvent> getEventClass() {
        return AgreementConfirmedEvent.class;
    }


    @Override
    public void handle(AgreementConfirmedEvent event) {
        ProcessAgreementConfirmedUseCase.Param param = new ProcessAgreementConfirmedUseCase.Param(
                event.getChatroomId(),
                event.getChatId(),
                event.getDisabledId(),
                event.getHelperId(),
                event.getCreatedAt()
        );

        processAgreementConfirmedUseCase.execute(param);
    }
}
