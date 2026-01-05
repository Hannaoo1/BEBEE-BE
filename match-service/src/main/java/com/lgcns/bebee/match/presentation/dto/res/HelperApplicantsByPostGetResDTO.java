package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetHelperApplicationsByPostUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperApplicantsByPostGetResDTO {
    private List<GetHelperApplicationsByPostUseCase.ApplicantInfo> applicants;

    public static HelperApplicantsByPostGetResDTO from(GetHelperApplicationsByPostUseCase.Result result) {
        return new HelperApplicantsByPostGetResDTO(result.getApplicants());
    }
}
