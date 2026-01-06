package com.lgcns.bebee.payment.common.exception;

import com.lgcns.bebee.common.exception.InvalidParamErrorInfo;

public enum PaymentInvalidParamErrors implements InvalidParamErrorInfo {
    REQUIRED_FIELD("필수 입력값입니다.", "field"),
    HONEY_USE_MUST_BE_POSITIVE("사용할 꿀은 0보다 큰 자연수여야 합니다.");

    private final String desc;
    private final String field;

    PaymentInvalidParamErrors(String desc) {
        this(desc, null);
    }

    PaymentInvalidParamErrors(String desc, String field) {
        this.desc = desc;
        this.field = field;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
