package com.lgcns.bebee.common.data.event.match;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AgreementConfirmedEvent implements DomainEvent {
    private final Long chatroomId;
    private final Long chatId;
    private final Long disabledId;
    private final Long helperId;
    private final LocalDateTime createdAt;

    private final Long matchId;
    private final Long agreementId;
    private final Long unitHoney;
    private final Long totalHoney;
    private final String type;
}
