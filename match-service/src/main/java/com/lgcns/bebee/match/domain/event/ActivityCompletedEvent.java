package com.lgcns.bebee.match.domain.event;

import com.lgcns.bebee.common.data.domain.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

/**
 * 활동 완료 이벤트
 *
 * 발행 시점:
 * - 케이스 1: 둘 다 완료 (즉시) → 둘 다 알림
 * - 케이스 2: 장애인만 완료 (즉시) → 도우미만 알림
 * - 케이스 3: 도우미만 완료 (3일 후) → 장애인만 알림
 *
 * 처리: "활동이 완료되었습니다" 푸시 알림
 */

@Getter
@RequiredArgsConstructor
public class ActivityCompletedEvent extends DomainEvent {
    private final Long engagementId;
    private final Long agreementId;
    private final Long helperId;
    private final Long disabledId;
    private final LocalDate activityDate;
    private final Boolean isLastActivity;

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getEventDesc() {
        return "활동 완료 이벤트";
    }
}
