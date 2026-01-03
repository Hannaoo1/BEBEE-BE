package com.lgcns.bebee.match.domain.entity.vo;

/**
 * 활동 상태
 * - PENDING: 대기 중 (초기 상태, 스케줄러 조회 대상)
 * - COMPLETED: 완료 (정산 대상)
 * - INCOMPLETED: 미완료 (환급 대상)
 */

public enum EngagementStatus {
    PENDING,        // 대기 중
    COMPLETED,      // 완료
    INCOMPLETED;    // 미완료
}
