package com.lgcns.bebee.common.data.event.member;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 뱃지 생성 이벤트
 *
 * 발행 시점: 활동 완료로 인해 도우미의 뱃지가 생성되었을 때
 * 처리: match-service에서 BadgeSync 테이블 업데이트 (2차)
 */
@Getter
@RequiredArgsConstructor
public class BadgeCreatedEvent implements DomainEvent {
    private final Long helperId;
    private final List<BadgeInfo> badges;


    @Getter
    @RequiredArgsConstructor
    public static class BadgeInfo {
        private final Long disabilityCategoryId;
        private final Integer completionCount;
        private final String badgeCode;
    }
}
