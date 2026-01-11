package com.lgcns.bebee.chat.infrastructure.event;

import com.lgcns.bebee.chat.application.ProcessAgreementRefusedUseCase;
import com.lgcns.bebee.common.data.event.match.AgreementRefusedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgreementRefusedEventHandler implements EventHandler<AgreementRefusedEvent> {
    private final ProcessAgreementRefusedUseCase processAgreementRefusedUseCase;

    @Override
    public EventType getEventType() { return EventType.AGREEMENT_REFUSED;}

    @Override
    public void handle(AgreementRefusedEvent event) {
        ProcessAgreementRefusedUseCase.Param param = new ProcessAgreementRefusedUseCase.Param(
                event.getChatroomId(),
                event.getChatId(),
                event.getDisabledId(),
                event.getHelperId(),
                event.getCreatedAt()
        );

        processAgreementRefusedUseCase.execute(param);
    }
}
