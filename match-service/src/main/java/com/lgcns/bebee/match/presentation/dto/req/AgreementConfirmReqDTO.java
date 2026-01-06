package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.ConfirmAgreementUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgreementConfirmReqDTO {
    private String disabledId;
    private String postId;
    private String title;
    private String chatroomId;

    public ConfirmAgreementUseCase.Param toParam(Long currentMemberId, String agreementId) {
        return new ConfirmAgreementUseCase.Param(
                currentMemberId,
                Long.parseLong(disabledId),
                Long.parseLong(postId),
                title,
                Long.parseLong(chatroomId),
                Long.parseLong(agreementId)
        );
    }
}
