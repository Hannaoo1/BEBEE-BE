package com.lgcns.bebee.common.data.event.match;
import com.lgcns.bebee.common.data.event.DomainEvent;
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
public class HoneyRefundEvent implements DomainEvent {
    private final Long agreementId;
    private final Long engagementId;
    private final Long disabledId;
    private final Integer amount;
    private final LocalDate activityDate;
}
