package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.ApplyHelperUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperApplyReqDTO {
    private String postId;
    private Boolean isVolunteer;

    public ApplyHelperUseCase.Param toParam(Long memberId, HelperApplyReqDTO request) {
        return new ApplyHelperUseCase.Param(
                memberId,
                Long.parseLong(request.getPostId()),
                request.getIsVolunteer()
        );
    }
}
