package com.lgcns.bebee.chat.infrastructure.event;

import com.lgcns.bebee.chat.application.UpdateChatroomMatchStatusUseCase;
import com.lgcns.bebee.chat.domain.entity.sync.MatchStatusSync;
import com.lgcns.bebee.common.data.event.AgreementConfirmedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgreementConfirmedEventHandler implements EventHandler<AgreementConfirmedEvent> {
    private final UpdateChatroomMatchStatusUseCase updateChatroomMatchStatusUseCase;

    @Override
    public EventType getEventType() {
        return EventType.AGREEMENT_CONFIRMED;
    }

    @Override
    public void handle(AgreementConfirmedEvent event) {
        UpdateChatroomMatchStatusUseCase.Param param = new UpdateChatroomMatchStatusUseCase.Param(event.getChatroomId(), MatchStatusSync.MATCHED);

        updateChatroomMatchStatusUseCase.execute(param);
    }
}
