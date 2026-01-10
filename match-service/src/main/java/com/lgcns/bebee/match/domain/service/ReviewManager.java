package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.repository.EngagementRepository;
import com.lgcns.bebee.match.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewManager {

    private final ReviewRepository reviewRepository;
    private final EngagementRepository engagementRepository;

    // 방향 결정
    public ReviewDirection determineReviewDirection(MemberSync member) {
        return (member.getRole() == Role.DISABLED)
                ? ReviewDirection.DISABLED_TO_HELPER
                : ReviewDirection.HELPER_TO_DISABLED;
    }

    // 키워드 검증
    public void validateKeywords(List<Integer> keywordIds, ReviewDirection direction) {
        List<Integer> validKeywordIds = Keyword.getByDirection(direction)
                .stream()
                .map(Keyword::getId)
                .toList();

        for (Integer keywordId : keywordIds) {
            if (!validKeywordIds.contains(keywordId)) {
                throw MatchErrors.INVALID_KEYWORD.toException();
            }
        }
    }

    @Transactional
    public Review createReview(
            Long engagementId,
            Long reviewerId,
            Long revieweeId,
            ReviewDirection direction,
            List<Integer> keywordIds
    ) {
        Engagement engagement = engagementRepository.findById(engagementId)
                .orElseThrow(MatchErrors.ENGAGEMENT_NOT_FOUND::toException);

        if (!EngagementStatus.COMPLETED.equals(engagement.getStatus())) {
            throw MatchErrors.ENGAGEMENT_NOT_COMPLETED.toException();
        }

        Match match = engagement.getMatch();
        if (match == null) {
            throw MatchErrors.MATCH_NOT_FOUND.toException();
        }

        if (direction == ReviewDirection.HELPER_TO_DISABLED && match.getHelperReview() != null) {
            throw MatchErrors.ALREADY_REVIEWED.toException();
        }
        if (direction == ReviewDirection.DISABLED_TO_HELPER && match.getDisabledReview() != null) {
            throw MatchErrors.ALREADY_REVIEWED.toException();
        }

        List<Engagement> allEngagements = engagementRepository.findAllByMatch_Id(match.getMatchId());
        Engagement lastEngagement = allEngagements.get(allEngagements.size() - 1);

        if (!engagement.getId().equals(lastEngagement.getId())) {
            throw MatchErrors.REVIEW_ONLY_FOR_LAST_ACTIVITY.toException();
        }

        Review review = Review.create(reviewerId, revieweeId, direction, keywordIds);
        review.setMatch(match);

        return reviewRepository.save(review);
    }
}