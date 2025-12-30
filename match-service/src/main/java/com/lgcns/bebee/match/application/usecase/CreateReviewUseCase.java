package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.repository.ReviewRepository;
import com.lgcns.bebee.match.domain.service.ReviewManager;
import com.lgcns.bebee.match.domain.service.ReviewValidator;
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

    private final ReviewRepository reviewRepository;
    private final ReviewManager reviewManager;
    private final ReviewValidator reviewValidator;

    @Transactional
    @Override
    public Result execute(Param param) {

        // 검증
        ReviewValidator.ValidationResult validation = reviewValidator.validateReviewEligibility(
                param.getEngagementId(),
                param.getReviewerId()
        );

        Match match = validation.getMatch();

        // 중복 확인
        if (reviewRepository.existsByEngagementIdAndReviewerId(
                param.getEngagementId(),
                param.getReviewerId()
        )) {
            throw MatchErrors.ALREADY_REVIEWED.toException();
        }

        // revieweeId 자동 결정 및 리뷰 방향 판단
        Long revieweeId;
        ReviewDirection direction;
        if (match.getDisabledId().equals(param.getReviewerId())) {
            revieweeId = match.getHelperId();
            direction = ReviewDirection.DISABLED_TO_HELPER;
        } else {
            revieweeId = match.getDisabledId();
            direction = ReviewDirection.HELPER_TO_DISABLED;
        }

        // 키워드 검증
        validateKeywords(param.getKeywordIds(), direction);

        // 리뷰 생성
        Review review = reviewManager.createReview(
                param.getEngagementId(),
                param.getReviewerId(),
                revieweeId,
                param.getKeywordIds()
        );

        return new Result(review.getId());
    }

    private void validateKeywords(List<Integer> keywordIds, ReviewDirection direction) {
        List<Keyword> allowedKeywords = Keyword.getByDirection(direction);
        List<Integer> allowedIds = allowedKeywords.stream()
                .map(Keyword::getId)
                .toList();

        for (Integer keywordId : keywordIds) {
            try {
                Keyword.fromId(keywordId);
            } catch (IllegalArgumentException e) {
                throw MatchErrors.INVALID_KEYWORD.toException();
            }

            if (!allowedIds.contains(keywordId)) {
                throw MatchErrors.KEYWORD_DIRECTION_MISMATCH.toException();
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long engagementId;
        private final Long reviewerId;
        private final List<Integer> keywordIds;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private Long reviewId;
    }
}
