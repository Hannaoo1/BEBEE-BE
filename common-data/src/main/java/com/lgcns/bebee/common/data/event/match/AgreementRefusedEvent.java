package com.lgcns.bebee.common.data.event.match;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AgreementRefusedEvent implements DomainEvent {
    private final Long chatroomId;
    private final Long chatId;
    private final Long disabledId;
    private final Long helperId;
    private final LocalDateTime createdAt;
}
