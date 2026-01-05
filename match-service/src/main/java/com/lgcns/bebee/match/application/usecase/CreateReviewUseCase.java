package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.repository.ReviewRepository;
import com.lgcns.bebee.match.domain.service.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static com.lgcns.bebee.match.domain.entity.QAgreement.agreement;

// 리뷰 작성 UseCase
@Service
@RequiredArgsConstructor
public class CreateReviewUseCase implements UseCase<CreateReviewUseCase.Param, CreateReviewUseCase.Result> {

    private final EngagementReader engagementReader;
    private final MatchReader matchReader;
    private final MemberManager memberManager;
    private final ReviewManager reviewManager;
    private final ReviewRepository reviewRepository;
    private final AgreementReader agreementReader;

    @Transactional
    @Override
    public Result execute(Param param) {
        param.validate();

        // Engagement 조회
        Engagement engagement = engagementReader.getById(param.getEngagementId());


        // 검증
        reviewManager.validateEngagementCompleted(engagement);
        reviewManager.validateLastActivity(engagement, agreement);
        reviewManager.validateNoDuplicateReview(param.getEngagementId(), param.getReviewerId());

        // Match 조회
        Match match = matchReader.getByAgreementId(engagement.getAgreementId());

        // 참여자 확인
        if (!match.isParticipant(param.getReviewerId())) {
            throw MatchErrors.NOT_ENGAGEMENT_MEMBER.toException();
        }

        // 작성자 조회
        MemberSync reviewer = memberManager.findExistingMember(param.getReviewerId());

        // reviewdirection 결정
        ReviewDirection direction = reviewManager.determineReviewDirection(reviewer);

        // revieweeId 결정
        Long revieweeId = determineRevieweeId(
                reviewer.getRole(),
                match.getHelperId(),
                match.getDisabledId()
        );

        // 키워드 검증 (Manager에 위임)
        reviewManager.validateKeywords(param.getKeywordIds(), direction);

        // 리뷰 생성
        Review review = Review.create(
                param.getEngagementId(),
                param.getReviewerId(),
                revieweeId,
                direction,
                param.getKeywordIds()
        );

        // 저장
        Review savedReview = reviewRepository.save(review);

        return Result.from(savedReview);
    }

    private Long determineRevieweeId(Role reviewerRole, Long helperId, Long disabledId) {
        if (reviewerRole == Role.DISABLED) {
            return helperId;
        } else {
            return disabledId;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long engagementId;
        private final Long reviewerId;
        private final List<Integer> keywordIds;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(engagementId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "engagementId");
            }
            if (!ParamValidator.isValidId(reviewerId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "reviewerId");
            }
            if (keywordIds == null || keywordIds.isEmpty()) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "keywordIds");
            }
            return true;
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private final Long reviewId;

        public static Result from(Review review) {
            return new Result(review.getId());
        }
    }
}