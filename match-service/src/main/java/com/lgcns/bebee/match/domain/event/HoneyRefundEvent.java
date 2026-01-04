package com.lgcns.bebee.match.domain.event;

import com.lgcns.bebee.common.data.domain.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

/**
 * 꿀 환급 이벤트
 *
 * 발행 시점: 케이스 4 (둘 다 미완료) - 3일 후 스케줄러
 * 처리: 장애인에게 꿀 환급
 */

@Getter
@RequiredArgsConstructor
public class HoneyRefundEvent extends DomainEvent {
    private final Long agreementId;
    private final Long engagementId;
    private final Long disabledId;
    private final Integer amount;
    private final LocalDate activityDate;

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getEventDesc() {
        return "꿀 환급 이벤트";
    }
}
