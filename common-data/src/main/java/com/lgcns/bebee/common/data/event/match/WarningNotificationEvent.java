package com.lgcns.bebee.common.data.event.match;
import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

/**
 * 경고 알림 이벤트
 *
 * 발행 시점: 케이스 4 (둘 다 미완료) - 2일 후 스케줄러
 * 처리: "1일 뒤 미완료 처리됩니다" 푸시 알림 (둘 다)
 */

@Getter
@RequiredArgsConstructor
public class WarningNotificationEvent implements DomainEvent {
    private final Long engagementId;
    private final Long agreementId;
    private final Long helperId;
    private final Long disabledId;
    private final LocalDate activityDate;
}