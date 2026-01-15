package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PasswordPolicyValidator {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{8,16}$");

    public void validate(String rawPassword) {
        if (rawPassword == null) {
            throw new InvalidParamException(MemberInvalidParamErrors.PASSWORD_NOT_NULL);
        }
        if (!PASSWORD_PATTERN.matcher(rawPassword).matches()) {
            throw new InvalidParamException(MemberInvalidParamErrors.INVALID_PASSWORD);
        }
    }
}

