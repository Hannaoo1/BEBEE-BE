package com.lgcns.bebee.gateway.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends DomainException{
    private final AuthenticationErrors error;

    public AuthenticationException(AuthenticationErrors error) {
        super(error);
        this.error = error;
    }
}
