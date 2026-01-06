package com.lgcns.bebee.match.infrastructure.spring;

import com.lgcns.bebee.match.application.usecase.UpdatePostLegalDongCodeUseCase;
import com.lgcns.bebee.match.domain.event.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@ConditionalOnProperty(name = "event.type", havingValue = "spring")
@RequiredArgsConstructor
public class SpringEventListener {
    private final UpdatePostLegalDongCodeUseCase updatePostLegalDongCodeUseCase;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPostCreated(PostCreatedEvent event) {
        UpdatePostLegalDongCodeUseCase.Param param = new UpdatePostLegalDongCodeUseCase.Param(
                event.getPostId(),
                event.getLatitude(),
                event.getLongitude()
        );

        updatePostLegalDongCodeUseCase.execute(param);
    }
}
