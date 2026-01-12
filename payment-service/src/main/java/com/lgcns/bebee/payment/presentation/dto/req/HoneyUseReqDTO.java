package com.lgcns.bebee.payment.presentation.dto.req;

import com.lgcns.bebee.payment.application.usecase.UseHoneyUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HoneyUseReqDTO {
    private Long memberId;
    private String matchId;
    private Long useHoney;

    public static UseHoneyUseCase.Param toParam(Long memberId, HoneyUseReqDTO request) {
        return new UseHoneyUseCase.Param(
                memberId,
                Long.parseLong(request.getMatchId()),
                request.getUseHoney()
        );
    }
}
