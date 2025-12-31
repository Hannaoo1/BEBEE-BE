package com.lgcns.bebee.match.infrastructure.event;

import com.lgcns.bebee.match.application.usecase.UpdatePostLegalDongCodeUseCase;
import com.lgcns.bebee.match.domain.event.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "event.type", havingValue = "spring")
@RequiredArgsConstructor
public class SpringEventListener {
    private final UpdatePostLegalDongCodeUseCase updatePostLegalDongCodeUseCase;

    @EventListener
    public void onPostCreated(PostCreatedEvent event) {
        UpdatePostLegalDongCodeUseCase.Param param = new UpdatePostLegalDongCodeUseCase.Param(
                event.getPostId(),
                event.getLatitude(),
                event.getLongitude()
        );

        updatePostLegalDongCodeUseCase.execute(param);
    }
}
