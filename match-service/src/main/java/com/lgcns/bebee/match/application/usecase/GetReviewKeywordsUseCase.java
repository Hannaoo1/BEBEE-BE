package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.service.EngagementReader;
import com.lgcns.bebee.match.domain.service.MatchReader;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetReviewKeywordsUseCase implements UseCase<GetReviewKeywordsUseCase.Param, GetReviewKeywordsUseCase.Result> {

    /*
     * 작성 가능한 키워드 목록 조회
     *
     * 양방향 리뷰:
     * - 장애인 → 도우미 평가: 키워드 1-12
     * - 도우미 → 장애인 평가: 키워드 13-24
     */

    private final EngagementReader engagementReader;
    private final MatchReader matchReader;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {

        // 활동 조회
        Engagement engagement = engagementReader.getById(param.getEngagementId());

        // 매칭 조회
        Match match = matchReader.getByAgreementId(engagement.getAgreementId());

        // 참여자 확인
        if (!match.isParticipant(param.getMemberId())) {
            throw MatchErrors.NOT_ENGAGEMENT_MEMBER.toException();
        }

        // 활동 완료 확인
        // Engagement 완료 API 구현 후 상태 및 종료일 검증 추가 예정
        if (!engagement.getIsDisabledCheck() || !engagement.getIsHelperCheck()) {
            throw MatchErrors.ENGAGEMENT_NOT_COMPLETED.toException();
        }

        // 리뷰 방향 결정
        ReviewDirection direction;
        if (match.getDisabledId().equals(param.getMemberId())) {
            direction = ReviewDirection.DISABLED_TO_HELPER;
        } else {
            direction = ReviewDirection.HELPER_TO_DISABLED;
        }

        // 방향에 맞는 키워드 목록 조회
        List<Keyword> keywords = Keyword.getByDirection(direction);

        // DTO 변환
        List<KeywordDTO> keywordDTOs = keywords.stream()
                .map(keyword -> new KeywordDTO(
                        keyword.getId(),
                        keyword.getDescription(),
                        keyword.isPositive()
                ))
                .toList();

        return new Result(keywordDTOs);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long engagementId;
        private final Long memberId;
    }

    @Getter
    @AllArgsConstructor
    public static class KeywordDTO {
        private Integer keywordId;
        private String description;
        private Boolean isPositive;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private List<KeywordDTO> keywords;
    }
}
