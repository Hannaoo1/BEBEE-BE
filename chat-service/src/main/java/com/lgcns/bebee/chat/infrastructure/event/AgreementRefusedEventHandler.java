package com.lgcns.bebee.chat.infrastructure.event;

import com.lgcns.bebee.chat.application.UpdateChatroomMatchStatusUseCase;
import com.lgcns.bebee.chat.domain.entity.sync.MatchStatusSync;
import com.lgcns.bebee.common.data.event.AgreementCreatedEvent;
import com.lgcns.bebee.common.data.event.AgreementRefusedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgreementRefusedEventHandler implements EventHandler<AgreementRefusedEvent> {
    private final UpdateChatroomMatchStatusUseCase updateChatroomMatchStatusUseCase;

    @Override
    public EventType getEventType() { return EventType.AGREEMENT_REFUSED;}

    @Override
    public void handle(AgreementRefusedEvent event) {
        UpdateChatroomMatchStatusUseCase.Param param = new UpdateChatroomMatchStatusUseCase.Param(event.getChatroomId(), MatchStatusSync.NON_MATCHED);

        updateChatroomMatchStatusUseCase.execute(param);
    }
}
