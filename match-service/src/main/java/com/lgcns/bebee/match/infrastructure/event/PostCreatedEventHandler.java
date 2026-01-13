package com.lgcns.bebee.match.infrastructure.event;

import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.match.PostCreatedEvent;
import com.lgcns.bebee.match.application.usecase.UpdatePostLegalDongCodeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostCreatedEventHandler implements EventHandler<PostCreatedEvent> {
    private final UpdatePostLegalDongCodeUseCase updatePostLegalDongCodeUseCase;

    @Override
    public Class<PostCreatedEvent> getEventClass() {
        return PostCreatedEvent.class;
    }

    @Override
    public void handle(PostCreatedEvent event) {
        UpdatePostLegalDongCodeUseCase.Param param = new UpdatePostLegalDongCodeUseCase.Param(
                event.getPostId(),
                event.getLatitude(),
                event.getLongitude()
        );

        updatePostLegalDongCodeUseCase.execute(param);
    }
}
