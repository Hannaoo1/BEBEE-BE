package com.lgcns.bebee.payment.core.exception;

import com.lgcns.bebee.common.exception.DomainException;

public class PaymentException extends DomainException {

    private final PaymentErrors error;

    public PaymentException(PaymentErrors error) {
        super(error);
        this.error = error;
    }

    public PaymentErrors getError() {
        return error;
    }
}