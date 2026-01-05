package com.lgcns.bebee.gateway.exception;

public class DomainException extends RuntimeException {
    public DomainException(ErrorInfo error){
        super(error.getMessage());
    }
}
