package com.lgcns.bebee.match.infrastructure.event;

import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.member.BadgeCreatedEvent;
import com.lgcns.bebee.match.application.usecase.UpdateBadgeSyncUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BadgeCreatedEventHandler implements EventHandler<BadgeCreatedEvent> {

    private final UpdateBadgeSyncUseCase updateBadgeSyncUseCase;

    @Override
    public Class<BadgeCreatedEvent> getEventClass() {
        return BadgeCreatedEvent.class;
    }

    @Override
    public void handle(BadgeCreatedEvent event) {
        log.info("BadgeCreatedEvent 수신 - helperId: {}, badges: {}",
                event.getHelperId(), event.getBadges().size());

        List<UpdateBadgeSyncUseCase.BadgeInfo> badges = event.getBadges().stream()
                .map(b -> new UpdateBadgeSyncUseCase.BadgeInfo(
                        b.getDisabilityCategoryId(),
                        b.getCompletionCount(),
                        b.getBadgeCode()
                ))
                .toList();

        UpdateBadgeSyncUseCase.Param param = new UpdateBadgeSyncUseCase.Param(
                event.getHelperId(),
                badges
        );

        updateBadgeSyncUseCase.execute(param);
    }
}
