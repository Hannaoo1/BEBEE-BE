package com.lgcns.bebee.common.data.event.match;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AgreementConfirmedEvent extends DomainEvent {
    private final Long chatroomId;
    private final Long chatId;
    private final Long disabledId;
    private final Long helperId;
    private final LocalDateTime createdAt;

    private final Long matchId;
    private final Long useHoney;

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getEventDesc() {
        return "매칭 확인서 수락 이벤트";
    }
}
