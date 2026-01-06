package com.lgcns.bebee.chat.infrastructure.event;

import com.lgcns.bebee.chat.application.UpdateChatroomMatchStatusUseCase;
import com.lgcns.bebee.chat.domain.entity.sync.MatchStatusSync;
import com.lgcns.bebee.common.data.event.AgreementCreatedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgreementCreatedEventHandler implements EventHandler<AgreementCreatedEvent> {
    private final UpdateChatroomMatchStatusUseCase updateChatroomMatchStatusUseCase;

    @Override
    public EventType getEventType() {
        return EventType.AGREEMENT_CREATED;
    }

    @Override
    public void handle(AgreementCreatedEvent event) {
        UpdateChatroomMatchStatusUseCase.Param param = new UpdateChatroomMatchStatusUseCase.Param(event.getChatroomId(), MatchStatusSync.PROCEEDING);

        updateChatroomMatchStatusUseCase.execute(param);
    }
}
