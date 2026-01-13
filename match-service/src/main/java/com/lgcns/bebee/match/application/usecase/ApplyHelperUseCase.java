package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.data.event.DomainEventPublisher;
import com.lgcns.bebee.common.data.event.match.PostAppliedEvent;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Application;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;
import com.lgcns.bebee.match.domain.repository.HelperApplicationRepository;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.PostManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyHelperUseCase implements UseCase<ApplyHelperUseCase.Param, Void> {

    private final MemberManager memberManager;
    private final PostManager postManager;
    private final HelperApplicationRepository applicationRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public Void execute(Param param) {
        param.validate();

        MemberSync member = memberManager.findExistingMember(param.getMemberId());
        if (member.getRole() != Role.HELPER) {
            throw MatchErrors.HELPER_ONLY_CAN_APPLY.toException();
        }

        if (applicationRepository.existsByApplicantIdAndPost_Id(param.getMemberId(), param.getPostId())) {
            throw MatchErrors.ALREADY_APPLIED.toException();
        }

        Post post = postManager.findSinglePost(param.getPostId());
        if (post.getStatus() == PostStatus.MATCHED) {
            throw MatchErrors.ALREADY_MATCHED.toException();
        }

        // 특정 게시물에 대해 어떤 멤버가 지원 -> 지원 객체 생성
        Application application = Application.create(
                param.getMemberId(),
                post,
                param.getIsVolunteer()
        );
        Application savedApplication = applicationRepository.save(application);

        eventPublisher.publish(new PostAppliedEvent(
                post.getMemberId(),
                savedApplication.getApplicantId(),
                savedApplication.getApplicationId(),
                savedApplication.getPost().getId(),
                savedApplication.getPost().getTitle()
                ));

        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final Long postId;
        private final Boolean isVolunteer;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(memberId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "helperId");
            }
            if (!ParamValidator.isValidId(postId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "disabledId");
            }
            if (!ParamValidator.isNotNull(isVolunteer)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "postId");
            }

            return true;
        }
    }
}
