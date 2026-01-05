package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.service.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 리뷰 키워드 목록 조회 UseCase
 *
 * 리뷰 작성 화면에 필요한 정보:
 * 게시글 제목, 도움 카테고리,
 * 닉네임, 리뷰 방향에 맞는 키워드 목록
 *
 */

@Service
@RequiredArgsConstructor
public class GetReviewKeywordsListUseCase implements UseCase<GetReviewKeywordsListUseCase.Param, GetReviewKeywordsListUseCase.Result> {

    private final EngagementReader engagementReader;
    private final AgreementReader agreementReader;
    private final MatchReader matchReader;
    private final MemberManager memberManager;
    private final ReviewManager reviewManager;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {
        // 파라미터 검증
        param.validate();

        // Engagement 조회
        Engagement engagement = engagementReader.getById(param.getEngagementId());

        // 활동 완료 확인
        if (engagement.getStatus() != EngagementStatus.COMPLETED) {
            throw MatchErrors.ENGAGEMENT_NOT_COMPLETED.toException();
        }

        // Agreement 조회 (도움 카테고리)
        Agreement agreement = agreementReader.getById(engagement.getAgreementId());

        // Match 조회 (참여자 정보 + 게시글 제목)
        Match match = matchReader.getByAgreementId(agreement.getId());

        // 참여자 확인
        if (!match.isParticipant(param.getReviewerId())) {
            throw MatchErrors.NOT_ENGAGEMENT_MEMBER.toException();
        }

        // 작성자 조회
        MemberSync reviewer = memberManager.findExistingMember(param.getReviewerId());

        // reviewdirection 결정
        ReviewDirection direction = reviewManager.determineReviewDirection(reviewer);

        // revieweeId 결정
        Long revieweeId = determineRevieweeId(
                param.getReviewerId(),
                match.getHelperId(),
                match.getDisabledId()
        );

        // 상대방 닉네임 조회
        MemberSync reviewee = memberManager.findExistingMember(revieweeId);

        // 도움 카테고리 변환
        List<HelpCategoryDTO> helpCategories = agreement.getHelpCategories().stream()
                .map(category -> new HelpCategoryDTO(
                        category.getId().getHelpCategoryId(),
                        category.getCategoryName()
                ))
                .collect(Collectors.toList());

        // 해당 방향의 키워드 목록
        List<Keyword> keywords = Keyword.getByDirection(direction);

        List<KeywordDTO> keywordDTOs = keywords.stream()
                .map(keyword -> new KeywordDTO(
                        keyword.getId(),
                        keyword.getDescription(),
                        keyword.isPositive()
                ))
                .collect(Collectors.toList());

        return new Result(
                match.getTitle(),
                helpCategories,
                reviewee.getNickname(),
                keywordDTOs
        );
    }

    // revieweeId 결정
    private Long determineRevieweeId(Long reviewerId, Long helperId, Long disabledId) {
        if (reviewerId.equals(disabledId)) {
            return helperId;
        } else {
            return disabledId;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long engagementId;
        private final Long reviewerId;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(engagementId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "engagementId");
            }
            if (!ParamValidator.isValidId(reviewerId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "reviewerId");
            }
            return true;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class HelpCategoryDTO {
        private Long helpCategoryId;
        private String categoryName;
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
        private String postTitle;
        private List<HelpCategoryDTO> helpCategories;
        private String revieweeName;
        private List<KeywordDTO> keywords;
    }
}