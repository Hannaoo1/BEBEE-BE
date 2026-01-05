package com.lgcns.bebee.payment.common.exception;

import com.lgcns.bebee.common.exception.DomainException;

public class PaymentException extends DomainException {
    private PaymentErrors error;

    public PaymentException(PaymentErrors error) {
        super(error);
        this.error = error;
    }

    public PaymentErrors getError() {
        return error;
    }
}
