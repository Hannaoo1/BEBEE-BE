package com.lgcns.bebee.member.presentation.dto.res;

import com.lgcns.bebee.member.application.usecase.SignUpUseCase;

public record SignUpResDTO(
        String memberId) {

    public static SignUpResDTO create(SignUpUseCase.Result result) {
        return new SignUpResDTO(String.valueOf(result.getMemberId()));
    }
}
