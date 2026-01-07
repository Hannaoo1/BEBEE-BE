package com.lgcns.bebee.payment.domain.entity.vo;

public enum EscrowStatus {
    PENDING,    // 임시 보관 중
    COMPLETED,  // 도우미에게 전달 완료
    REFUNDED    // 환불 완료 (매칭 취소 시)
}
