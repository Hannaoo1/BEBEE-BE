package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewManager {

    private final ReviewRepository reviewRepository;

    // 활동 완료 검증
    public void validateEngagementCompleted(Engagement engagement) {
        if (engagement.getStatus() != EngagementStatus.COMPLETED) {
            throw MatchErrors.ENGAGEMENT_NOT_COMPLETED.toException();
        }
    }


    // 중복 리뷰 검증
    public void validateNoDuplicateReview(Long engagementId, Long reviewerId) {
        if (reviewRepository.existsByEngagementIdAndReviewerId(engagementId, reviewerId)) {
            throw MatchErrors.ALREADY_REVIEWED.toException();
        }
    }

    // ReviewDirection 결정
    public ReviewDirection determineReviewDirection(MemberSync reviewer) {
        if (reviewer.getRole() == Role.DISABLED) {
            return ReviewDirection.DISABLED_TO_HELPER;
        } else {
            return ReviewDirection.HELPER_TO_DISABLED;
        }
    }

    // 키워드 검증 (유효성 + 방향 일치)
    public void validateKeywords(List<Integer> keywordIds, ReviewDirection direction) {
        if (keywordIds == null || keywordIds.isEmpty()) {
            throw MatchErrors.INVALID_KEYWORD.toException();
        }

        for (Integer keywordId : keywordIds) {
            Keyword keyword = Keyword.fromId(keywordId);

            if (keyword.getDirection() != direction) {
                throw MatchErrors.KEYWORD_DIRECTION_MISMATCH.toException();
            }
        }
    }
}