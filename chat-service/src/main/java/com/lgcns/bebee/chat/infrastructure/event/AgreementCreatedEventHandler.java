package com.lgcns.bebee.chat.infrastructure.event;

import com.lgcns.bebee.chat.application.ProcessAgreementCreatedUseCase;
import com.lgcns.bebee.common.data.event.match.AgreementCreatedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgreementCreatedEventHandler implements EventHandler<AgreementCreatedEvent> {
    private final ProcessAgreementCreatedUseCase processAgreementCreatedUseCase;

    @Override
    public Class<AgreementCreatedEvent> getEventClass() {
        return AgreementCreatedEvent.class;
    }


    @Override
    public void handle(AgreementCreatedEvent event) {
        ProcessAgreementCreatedUseCase.Param param = new ProcessAgreementCreatedUseCase.Param(
                event.getChatroomId(),
                event.getAgreementId(),
                event.getDisabledId(),
                event.getHelperId(),
                event.getType(),
                event.getIsVolunteer(),
                event.getStartDate(),
                event.getEndDate(),
                event.getSchedules(),
                event.getRegion(),
                event.getUnitHoney(),
                event.getTotalHoney(),
                event.getHelpCategoryIds(),
                event.getCreatedAt()
        );

        processAgreementCreatedUseCase.execute(param);
    }
}
