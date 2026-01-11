package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.RefuseAgreementUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgreementRefuseReqDTO {
    private String disabledId;
    private String chatroomId;
    private String chatId;
    private LocalDateTime createdAt;

    public RefuseAgreementUseCase.Param toParam(Long currentMemberId, String agreementId) {
        return new RefuseAgreementUseCase.Param(
                currentMemberId,
                Long.parseLong(disabledId),
                Long.parseLong(agreementId),
                Long.parseLong(chatroomId),
                Long.parseLong(chatId),
                createdAt
        );
    }
}
