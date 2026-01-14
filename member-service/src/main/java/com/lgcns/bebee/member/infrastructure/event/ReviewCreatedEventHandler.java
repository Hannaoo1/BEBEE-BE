package com.lgcns.bebee.member.infrastructure.event;

import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.match.ReviewCreatedEvent;
import com.lgcns.bebee.member.application.usecase.SyncReviewUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewCreatedEventHandler implements EventHandler<ReviewCreatedEvent> {

    private final SyncReviewUseCase syncReviewUseCase;

    @Override
    public Class<ReviewCreatedEvent> getEventClass() {
        return ReviewCreatedEvent.class;
    }

    @Override
    @Transactional
    public void handle(ReviewCreatedEvent event) {
        log.info("ReviewCreated 이벤트 처리 시작 - reviewId: {}, revieweeId: {}, keywordCount: {}",
                event.getReviewId(), event.getRevieweeId(), event.getKeywordIds().size());

        SyncReviewUseCase.Param param = new SyncReviewUseCase.Param(
                event.getReviewId(),
                event.getMatchId(),
                event.getReviewerId(),
                event.getRevieweeId(),
                event.getReviewDirection(),
                event.getKeywordIds()
        );

        syncReviewUseCase.execute(param);

        log.info("ReviewCreated 이벤트 처리 완료 - reviewId: {}", event.getReviewId());
    }
}
