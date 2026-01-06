package com.lgcns.bebee.common.data.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AgreementCreatedEvent extends DomainEvent {
    private final Long chatroomId;

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getEventDesc() {
        return "매칭 확인서 생성 이벤트";
    }
}
