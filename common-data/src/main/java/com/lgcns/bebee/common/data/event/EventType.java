package com.lgcns.bebee.common.data.event;

import com.lgcns.bebee.common.data.event.match.AgreementConfirmedEvent;
import com.lgcns.bebee.common.data.event.match.AgreementCreatedEvent;
import com.lgcns.bebee.common.data.event.match.AgreementRefusedEvent;
import com.lgcns.bebee.common.data.event.match.PostCreatedEvent;
import com.lgcns.bebee.common.data.event.member.BadgeCreatedEvent;
import com.lgcns.bebee.common.data.event.EngagementCompletedEvent;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EventType {
    AGREEMENT_CREATED("AgreementCreatedEvent", AgreementCreatedEvent.class),
    AGREEMENT_CONFIRMED("AgreementConfirmedEvent", AgreementConfirmedEvent.class),
    AGREEMENT_REFUSED("AgreementRefusedEvent", AgreementRefusedEvent.class),

    POST_CREATED("PostCreatedEvent", PostCreatedEvent.class),
    ENGAGEMENT_COMPLETED("EngagementCompletedEvent", EngagementCompletedEvent.class),
    BADGE_CREATED("BadgeCreatedEvent", BadgeCreatedEvent.class),
    ;

    private final String eventName;
    private final Class<? extends DomainEvent> eventClass;

    EventType(String eventName, Class<? extends DomainEvent> eventClass) {
        this.eventName = eventName;
        this.eventClass = eventClass;
    }

    public static EventType from(String name) {
        return Arrays.stream(EventType.values())
                .filter(type -> type.getEventName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 이벤트 타입입니다.: " + name));
    }
}
