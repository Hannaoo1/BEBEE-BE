package com.lgcns.bebee.common.data.event.chat;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatroomCreatedEvent implements DomainEvent {
    private final Long disabledId;
    private final String disabledNickname;
    private final Long helperId;
    private final Long chatroomId;
}
