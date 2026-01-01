package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.repository.EngagementRepository;
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

    private final ReviewManager reviewManager;
    private final EngagementRepository engagementRepository;
    private final MemberManager memberManager;

    @Transactional
    @Override
    public Result execute(Param param) {

        // Engagement 조회
        Engagement engagement = engagementRepository.findById(param.getEngagementId())
                .orElseThrow(MatchErrors.ENGAGEMENT_NOT_FOUND::toException);

        // 활동 완료 검증
        reviewManager.validateEngagementCompleted(engagement);

        // 중복 리뷰 검증
        reviewManager.validateNoDuplicateReview(
                param.getEngagementId(),
                param.getReviewerId()
        );

        // 작성자 조회
        MemberSync reviewer = memberManager.findExistingMember(param.getReviewerId());

        // ReviewDirection 결정
        ReviewDirection direction = reviewManager.determineReviewDirection(reviewer);

        // 키워드 검증
        validateKeywords(param.getKeywordIds());

        // 리뷰 생성
        Review review = reviewManager.createReview(
                param.getEngagementId(),
                param.getReviewerId(),
                param.getRevieweeId(),
                direction,
                param.getKeywordIds()
        );

        return new Result(review.getId());
    }
    
    // 키워드 유효성 검증
    private void validateKeywords(List<Integer> keywordIds) {
        if (keywordIds == null || keywordIds.isEmpty()) {
            throw MatchErrors.INVALID_KEYWORD.toException();
        }

        for (Integer keywordId : keywordIds) {
            Keyword.fromId(keywordId);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long engagementId;
        private final Long reviewerId;
        private final Long revieweeId;
        private final List<Integer> keywordIds;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private Long reviewId;
    }
}
