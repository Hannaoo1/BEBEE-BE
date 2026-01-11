package com.lgcns.bebee.common.data.event.match;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AgreementConfirmedEvent extends DomainEvent {
    private final Long chatroomId;
<<<<<<< HEAD:common-data/src/main/java/com/lgcns/bebee/common/data/event/match/AgreementConfirmedEvent.java
    private final Long chatId;
=======

    private final Long helperId;
>>>>>>> 8e0a30f (feat: AgreementConfirmed 이벤트 처리 및 Payment sync 로직 구현):common-data/src/main/java/com/lgcns/bebee/common/data/event/AgreementConfirmedEvent.java
    private final Long disabledId;
    private final Long helperId;
    private final LocalDateTime createdAt;

    private final Long matchId;
    private final Long agreementId;
    private final Long unitHoney;
    private final Long totalHoney;
    private final String type;

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getEventDesc() {
        return "매칭 확인서 수락 이벤트";
    }
}
