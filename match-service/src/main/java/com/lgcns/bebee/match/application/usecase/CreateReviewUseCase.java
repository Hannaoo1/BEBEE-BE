package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.data.event.DomainEventPublisher;
import com.lgcns.bebee.common.data.event.match.ReviewCreatedEvent;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;

import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.ReviewManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CreateReviewUseCase implements UseCase<CreateReviewUseCase.Param, CreateReviewUseCase.Result> {

    private final MemberManager memberManager;
    private final ReviewManager reviewManager;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    @Override
    public Result execute(Param param) {
        param.validate();

        // 회원 조회
        MemberSync reviewer = memberManager.findExistingMember(param.getReviewerId());

        // ReviewDirection 결정
        ReviewDirection direction = reviewManager.determineReviewDirection(reviewer);

        // 키워드 검증
        reviewManager.validateKeywords(param.getKeywordIds(), direction);

        // 리뷰 생성 & 저장
        Review review = reviewManager.createReview(
                param.getMatchId(),
                param.getReviewerId(),
                direction,
                param.getKeywordIds()
        );

        // 리뷰 생성 이벤트 발행
        eventPublisher.publish(new ReviewCreatedEvent(
                review.getId(),
                review.getMatch().getMatchId(),
                review.getReviewerId(),
                review.getRevieweeId(),
                review.getReviewDirection().name(),
                param.getKeywordIds()
        ));

        return Result.success();
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long matchId;
        private final Long reviewerId;
        private final List<Integer> keywordIds;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(matchId)) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.REQUIRED_FIELD,
                        "matchId"
                );
            }
            if (!ParamValidator.isValidId(reviewerId)) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.REQUIRED_FIELD,
                        "reviewerId"
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
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private final String message;

        public static Result success() {
            return new Result("리뷰가 작성되었습니다");
        }
    }
}