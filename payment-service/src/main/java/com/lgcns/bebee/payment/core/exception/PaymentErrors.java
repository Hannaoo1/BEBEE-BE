package com.lgcns.bebee.payment.core.exception;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.ErrorInfo;

public enum PaymentErrors implements ErrorInfo {
    // 결제 관련
    PAYMENT_NOT_FOUND("결제 정보를 찾을 수 없습니다."),
    PAYMENT_ALREADY_EXISTS("이미 처리된 결제입니다."),
    PAYMENT_ALREADY_CANCELED("이미 취소된 결제입니다."),
    PAYMENT_CANNOT_CANCEL("취소할 수 없는 결제 상태입니다."),
    PAYMENT_AMOUNT_MISMATCH("결제 금액이 일치하지 않습니다."),
    PAYMENT_MEMBER_MISMATCH("결제 회원 정보가 일치하지 않습니다."),
    INVALID_PAYMENT_AMOUNT("유효하지 않은 결제 금액입니다."),

    // 허니 관련
    HONEY_WALLET_NOT_FOUND("허니 지갑을 찾을 수 없습니다."),
    INSUFFICIENT_HONEY_BALANCE("허니 잔액이 부족합니다."),

    // 토스 API
    TOSS_API_ERROR("토스페이먼츠 API 호출에 실패했습니다."),
    TOSS_API_TIMEOUT("토스페이먼츠 API 요청 시간이 초과되었습니다."),
    TOSS_WEBHOOK_INVALID_SIGNATURE("웹훅 서명 검증에 실패했습니다."),

    // 권한
    PAYMENT_ACCESS_DENIED("결제 정보에 접근할 권한이 없습니다.");

    private final String desc;

    PaymentErrors(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public DomainException toException() {
        return new PaymentException(this);
    }
}