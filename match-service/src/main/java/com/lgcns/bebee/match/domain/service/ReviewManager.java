package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import com.lgcns.bebee.match.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewManager {

    private final MatchRepository matchRepository;
    private final ReviewRepository reviewRepository;

    public Review findById(long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> MatchErrors.REVIEW_NOT_FOUND.toException());
    }

    // ReviewDirection 결정
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

    // 리뷰 생성 및 검증
    public Review createReview(
            Long matchId,
            Long reviewerId,
            ReviewDirection direction,
            List<Integer> keywordIds
    ) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> MatchErrors.MATCH_NOT_FOUND.toException());

        // 상대방(reviewee) 자동 결정: 내가 아닌 사람
        Long revieweeId = match.getHelperId().equals(reviewerId)
                ? match.getDisabledId()
                : match.getHelperId();

        Review review = Review.create(
                match,
                reviewerId,
                revieweeId,
                direction,
                keywordIds
        );

        return reviewRepository.save(review);
    }
}