package com.lgcns.bebee.common.data.event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

/**
 * 꿀 정산 이벤트
 *
 * 발행 시점:
 * - 케이스 1: 둘 다 완료 체크 (즉시)
 * - 케이스 2: 장애인만 완료 체크 (즉시)
 * - 케이스 3: 스케줄러 (3일 후 자동 완료)
 *
 * 처리:
 * - 장애인 지갑: -amount 꿀
 * - 도우미 지갑: +amount 꿀
 *
 */

@Getter
@RequiredArgsConstructor
public class HoneySettlementEvent extends DomainEvent {
    private final Long agreementId;
    private final Long engagementId;
    private final Long helperId;
    private final Long disabledId;
    private final Integer amount;
    private final LocalDate activityDate;

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getEventDesc() {
        return "꿀 정산 이벤트";
    }
}
