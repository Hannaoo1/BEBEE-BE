package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.ReviewManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CreateReviewUseCase implements UseCase<CreateReviewUseCase.Param, CreateReviewUseCase.Result> {

    private final MemberManager memberManager;
    private final ReviewManager reviewManager;

    @Override
    public Result execute(Param param) {
        param.validate();

        MemberSync reviewer = memberManager.findExistingMember(param.getReviewerId());

        // ReviewDirection 결정
        ReviewDirection direction = reviewManager.determineReviewDirection(reviewer);

        // 키워드 검증
        reviewManager.validateKeywords(param.getKeywordIds(), direction);

        // 리뷰 생성
        Review review = reviewManager.createReview(
                param.getEngagementId(),
                param.getReviewerId(),
                param.getRevieweeId(),
                direction,
                param.getKeywordIds()
        );

        return Result.from(review);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long engagementId;
        private final Long reviewerId;
        private final Long revieweeId;
        private final List<Integer> keywordIds;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(engagementId)) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.REQUIRED_FIELD,
                        "engagementId"
                );
            }

            if (!ParamValidator.isValidId(reviewerId)) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.REQUIRED_FIELD,
                        "reviewerId"
                );
            }

            if (!ParamValidator.isValidId(revieweeId)) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.REQUIRED_FIELD,
                        "revieweeId"
                );
            }

            if (keywordIds == null || keywordIds.isEmpty()) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.REQUIRED_FIELD,
                        "keywordIds"
                );
            }

            return true;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Result {
        private final String message;

        public static Result from(Review review) {
            return new Result("리뷰가 작성되었습니다");
        }
    }
}