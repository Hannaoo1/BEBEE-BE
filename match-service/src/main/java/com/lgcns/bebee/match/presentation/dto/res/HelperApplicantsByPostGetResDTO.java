package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetHelperApplicationsByPostUseCase;
import com.lgcns.bebee.match.domain.entity.sync.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperApplicantsByPostGetResDTO {
    private List<ApplicantDTO> applicants;

    public static HelperApplicantsByPostGetResDTO from(GetHelperApplicationsByPostUseCase.Result result) {
        List<ApplicantDTO> applicants = result.getApplicants().stream()
                .map(ApplicantDTO::from).toList();

        return new HelperApplicantsByPostGetResDTO(applicants);
    }

    @Getter
    @AllArgsConstructor
    public static class ApplicantDTO {
        private String memberId;
        private String nickname;
        private Integer ageGroup;
        private Gender gender;
        private Boolean isVolunteer;
        private List<BadgeDTO> badges;

        public static ApplicantDTO from(GetHelperApplicationsByPostUseCase.ApplicantInfo applicant) {
            List<BadgeDTO> badges = applicant.getBadges().stream()
                    .map(badge -> new BadgeDTO(
                            badge.getDisabilityCategoryId(),
                            badge.getCompletionCount(),
                            badge.getBadgeCode()
                    ))
                    .toList();

            return new ApplicantDTO(
                    String.valueOf(applicant.getMemberId()),
                    applicant.getNickname(),
                    applicant.getAgeGroup(),
                    applicant.getGender(),
                    applicant.getIsVolunteer(),
                    badges
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class BadgeDTO {
        private Long disabilityCategoryId;
        private Integer completionCount;
        private String badgeCode;
    }
}
