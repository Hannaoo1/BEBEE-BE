package com.lgcns.bebee.match.domain.event;

import com.lgcns.bebee.common.data.domain.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

/**
 * 리뷰 요청 이벤트
 *
 * 발행 시점: 마지막 활동 완료 시
 * - DAY: 활동 1개 완료 시 (항상 마지막)
 * - TERM: 마지막 활동 완료 시 (나중에 구현)
 *
 * 처리: "리뷰를 작성해주세요" 푸시 알림 (둘 다)
 *
 * 리뷰 대상: 완료된 활동들 (engagementIds)
 */

@Getter
@RequiredArgsConstructor
public class ReviewRequestEvent extends DomainEvent {
    private final Long agreementId;
    private final List<Long> engagementIds;
    private final Long helperId;
    private final Long disabledId;

    @Override
    public String getEventName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getEventDesc() {
        return "리뷰 요청 이벤트";
    }
}

