package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReviewValidator {
    
    // 리뷰 검증
    private final EngagementReader engagementReader;
    private final MatchReader matchReader;

    // 리뷰 작성 가능
    @Transactional(readOnly = true)
    public ValidationResult validateReviewEligibility(Long engagementId, Long memberId) {

        // 활동 조회
        Engagement engagement = engagementReader.getById(engagementId);

        // 매칭 조회
        Match match = matchReader.getByAgreementId(engagement.getAgreementId());

        // 참여자 확인
        if (!match.isParticipant(memberId)) {
            throw MatchErrors.NOT_ENGAGEMENT_MEMBER.toException();
        }

        // 활동 완료 확인
        validateEngagementCompleted(engagement);

        return new ValidationResult(engagement, match);
    }

    private void validateEngagementCompleted(Engagement engagement) {
        // 상태 확인
        if (engagement.getStatus() != EngagementStatus.COMPLETED) {
            throw MatchErrors.ENGAGEMENT_NOT_COMPLETED.toException();
        }

        // 종료 날짜 확인
        if (LocalDate.now().isBefore(engagement.getEndDate())
                || LocalDate.now().equals(engagement.getEndDate())) {
            throw MatchErrors.ENGAGEMENT_NOT_ENDED.toException();
        }
    }

    // 검증 결과
    @lombok.Getter
    @lombok.AllArgsConstructor
    public static class ValidationResult {
        private Engagement engagement;
        private Match match;
    }
}
