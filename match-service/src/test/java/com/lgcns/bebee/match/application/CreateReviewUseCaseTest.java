package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchException;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.repository.ReviewRepository;
import com.lgcns.bebee.match.domain.service.EngagementReader;
import com.lgcns.bebee.match.domain.service.MatchReader;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.ReviewManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("리뷰 작성 유스케이스 테스트")
class CreateReviewUseCaseTest {

    @Mock
    private ReviewManager reviewManager;

    @Mock
    private EngagementReader engagementReader;

    @Mock
    private MemberManager memberManager;

    @Mock
    private MatchReader matchReader;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private CreateReviewUseCase useCase;

    @Mock
    private Engagement engagement;

    @Mock
    private Match match;

    @Mock
    private MemberSync memberSync;

    @Mock
    private Review review;

    @Test
    @DisplayName("장애인이 도우미를 평가하는 리뷰 작성 성공")
    void execute_disabledReviewsHelper_success() {
        // Given
        Long engagementId = 1L;
        Long reviewerId = 100L;  // 장애인
        Long helperId = 700L;    // 도우미
        List<Integer> keywordIds = Arrays.asList(1, 3, 5);

        CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                engagementId,
                reviewerId,
                keywordIds
        );

        // Engagement 설정
        given(engagementReader.getById(engagementId)).willReturn(engagement);
        given(engagement.getStatus()).willReturn(EngagementStatus.COMPLETED);
        given(engagement.getAgreementId()).willReturn(1L);

        // 중복 검증 통과
        doNothing().when(reviewManager).validateEngagementCompleted(any());
        doNothing().when(reviewManager).validateNoDuplicateReview(anyLong(), anyLong());

        // Match 설정
        given(matchReader.getByAgreementId(1L)).willReturn(match);
        given(match.isParticipant(reviewerId)).willReturn(true);
        given(match.getHelperId()).willReturn(helperId);
        given(match.getDisabledId()).willReturn(reviewerId);

        // MemberSync 설정 (장애인)
        given(memberManager.findExistingMember(reviewerId)).willReturn(memberSync);
        given(memberSync.getRole()).willReturn(Role.DISABLED);

        // ReviewDirection 설정
        given(reviewManager.determineReviewDirection(memberSync))
                .willReturn(ReviewDirection.DISABLED_TO_HELPER);

        // Review 생성
        given(review.getId()).willReturn(1L);
        given(reviewManager.createReview(
                engagementId,
                reviewerId,
                helperId,
                ReviewDirection.DISABLED_TO_HELPER,
                keywordIds
        )).willReturn(review);

        // When
        CreateReviewUseCase.Result result = useCase.execute(param);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getReviewId());
        verify(engagementReader).getById(engagementId);
        verify(reviewManager).validateEngagementCompleted(engagement);
        verify(reviewManager).validateNoDuplicateReview(engagementId, reviewerId);
        verify(matchReader).getByAgreementId(1L);
        verify(memberManager).findExistingMember(reviewerId);
        verify(reviewManager).createReview(
                engagementId,
                reviewerId,
                helperId,
                ReviewDirection.DISABLED_TO_HELPER,
                keywordIds
        );
    }

    @Test
    @DisplayName("도우미가 장애인을 평가하는 리뷰 작성 성공")
    void execute_helperReviewsDisabled_success() {
        // Given
        Long engagementId = 1L;
        Long reviewerId = 700L;  // 도우미
        Long disabledId = 100L;  // 장애인
        List<Integer> keywordIds = Arrays.asList(15, 17, 19);

        CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                engagementId,
                reviewerId,
                keywordIds
        );

        given(engagementReader.getById(engagementId)).willReturn(engagement);
        given(engagement.getStatus()).willReturn(EngagementStatus.COMPLETED);
        given(engagement.getAgreementId()).willReturn(1L);

        doNothing().when(reviewManager).validateEngagementCompleted(any());
        doNothing().when(reviewManager).validateNoDuplicateReview(anyLong(), anyLong());

        given(matchReader.getByAgreementId(1L)).willReturn(match);
        given(match.isParticipant(reviewerId)).willReturn(true);
        given(match.getHelperId()).willReturn(reviewerId);
        given(match.getDisabledId()).willReturn(disabledId);

        given(memberManager.findExistingMember(reviewerId)).willReturn(memberSync);
        given(memberSync.getRole()).willReturn(Role.HELPER);

        given(reviewManager.determineReviewDirection(memberSync))
                .willReturn(ReviewDirection.HELPER_TO_DISABLED);

        given(review.getId()).willReturn(2L);
        given(reviewManager.createReview(
                engagementId,
                reviewerId,
                disabledId,
                ReviewDirection.HELPER_TO_DISABLED,
                keywordIds
        )).willReturn(review);

        // When
        CreateReviewUseCase.Result result = useCase.execute(param);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getReviewId());
        verify(reviewManager).createReview(
                engagementId,
                reviewerId,
                disabledId,
                ReviewDirection.HELPER_TO_DISABLED,
                keywordIds
        );
    }
}