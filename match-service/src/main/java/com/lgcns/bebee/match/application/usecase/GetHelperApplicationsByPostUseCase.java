package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.AgeGroupCalculator;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Application;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.sync.Gender;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.repository.HelperApplicationRepository;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.PostManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetHelperApplicationsByPostUseCase implements UseCase<GetHelperApplicationsByPostUseCase.Param, GetHelperApplicationsByPostUseCase.Result> {

    private final PostManager postManager;
    private final HelperApplicationRepository applicationRepository;
    private final MemberManager memberManager;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {
        param.validate();
        
        Post post = postManager.findSinglePost(param.getPostId());
        
        if (!post.getMemberId().equals(param.getMemberId())) {
            throw MatchErrors.UNAUTHORIZED_ACCESS.toException();
        }
        
        List<Application> applications = applicationRepository.findAllByPost_Id(param.getPostId());
        
        List<ApplicantInfo> applicants = applications.stream()
                .map(application -> {
                    MemberSync member = memberManager.findExistingMember(application.getApplicantId());

                    Integer ageGroup = AgeGroupCalculator.calculateAgeGroup(member.getBirthDate());

                    return new ApplicantInfo(
                            member.getId(),
                            member.getNickname(),
                            ageGroup,
                            member.getGender(),
                            application.getIsVolunteer()
                    );
                })
                .collect(Collectors.toList());

        return new Result(applicants);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final Long postId;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(memberId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "memberId");
            }
            if (!ParamValidator.isValidId(postId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "postId");
            }
            return true;
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private List<ApplicantInfo> applicants;
    }

    @Getter
    @AllArgsConstructor
    public static class ApplicantInfo {
        private Long memberId;
        private String nickname;
        private Integer ageGroup;
        private Gender gender;
        private Boolean isVolunteer;
    }
}