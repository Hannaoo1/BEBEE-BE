package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewManager {
    private final ReviewRepository reviewRepository;

    // 리뷰 생성
    @Transactional
    public Review createReview(
            Long engagementId,
            Long reviewerId,
            Long revieweeId,
            ReviewDirection reviewDirection,
            List<Integer> keywordIds
    ) {
        Review review = Review.create(
                engagementId,
                reviewerId,
                revieweeId,
                reviewDirection,
                keywordIds
        );

        return reviewRepository.save(review);
    }
    
    // 중복 리뷰 검증
    @Transactional(readOnly = true)
    public void validateNoDuplicateReview(Long engagementId, Long reviewerId) {
        if (reviewRepository.existsByEngagementIdAndReviewerId(engagementId, reviewerId)) {
            throw MatchErrors.ALREADY_REVIEWED.toException();
        }
    }

    // 활동 완료 검증
    public void validateEngagementCompleted(Engagement engagement) {
        if (engagement.getStatus() != EngagementStatus.COMPLETED) {
            throw MatchErrors.ENGAGEMENT_NOT_COMPLETED.toException();
        }
    }

    // ReviewDirection 결정
    public ReviewDirection determineReviewDirection(MemberSync reviewer) {
        return reviewer.getRole() == Role.DISABLED
                ? ReviewDirection.DISABLED_TO_HELPER
                : ReviewDirection.HELPER_TO_DISABLED;
    }

    // 받은 리뷰 조회
    @Transactional(readOnly = true)
    public List<Review> findReceivedReviews(Long revieweeId) {
        return reviewRepository.findReceivedReviewsWithKeywords(revieweeId);
    }
}
