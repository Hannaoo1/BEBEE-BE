package com.lgcns.bebee.match.domain.event;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreatedEvent extends DomainEvent {
    private final Long postId;
    private final Double latitude;
    private final Double longitude;

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getEventDesc() {
        return "게시글 생성 이벤트";
    }
}
