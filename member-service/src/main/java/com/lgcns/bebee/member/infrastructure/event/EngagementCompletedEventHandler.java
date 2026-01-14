package com.lgcns.bebee.member.infrastructure.event;

import com.lgcns.bebee.common.data.event.match.EngagementCompletedEvent;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.member.application.usecase.CreateBadgeUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
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
        log.info("EngagementCompleted 이벤트 처리 시작 - engagementId: {}, helperId: {}, disabledId: {}",
                event.getEngagementId(), event.getHelperId(), event.getDisabledId());

        CreateBadgeUseCase.Param param = new CreateBadgeUseCase.Param(
                event.getHelperId(),
                event.getDisabledId()
        );
        createBadgeUseCase.execute(param);

        log.info("EngagementCompleted 이벤트 처리 완료 - helperId: {}", event.getHelperId());
    }
}
