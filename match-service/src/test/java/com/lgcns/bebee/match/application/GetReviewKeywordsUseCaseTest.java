package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.GetReviewKeywordsUseCase;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchException;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.service.ReviewValidator;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("리뷰 키워드 목록 조회 유스케이스 테스트")
class GetReviewKeywordsUseCaseTest {

    @Mock
    private ReviewValidator reviewValidator;

    @InjectMocks
    private GetReviewKeywordsUseCase useCase;

    private Long engagementId;
    private Long disabledId;
    private Long helperId;

    @BeforeEach
    void setUp() {
        engagementId = 79L;
        disabledId = 100L;
        helperId = 200L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("장애인이 조회 시 도우미 평가용 키워드 반환")
        void shouldReturnHelperKeywords_whenDisabledUserRequests() {
            // Given
            GetReviewKeywordsUseCase.Param param = new GetReviewKeywordsUseCase.Param(engagementId, disabledId);

            Match mockMatch = Mockito.mock(Match.class);
            Mockito.when(mockMatch.getDisabledId()).thenReturn(disabledId);
            Mockito.when(mockMatch.getHelperId()).thenReturn(helperId);

            ReviewValidator.ValidationResult validation = new ReviewValidator.ValidationResult(mockMatch);

            Mockito.when(reviewValidator.validateReviewEligibility(engagementId, disabledId))
                    .thenReturn(validation);

            // ⭐ 여기도 Mockito.when()으로 통일!
            Mockito.when(reviewValidator.validateReviewEligibility(engagementId, disabledId))
                    .thenReturn(validation);

            // When
            GetReviewKeywordsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getKeywords()).isNotEmpty();
            assertThat(result.getKeywords().size()).isGreaterThan(0);

            GetReviewKeywordsUseCase.KeywordDTO firstKeyword = result.getKeywords().get(0);
            assertThat(firstKeyword.getKeywordId()).isNotNull();
            assertThat(firstKeyword.getDescription()).isNotBlank();
            assertThat(firstKeyword.getIsPositive()).isNotNull();

            Mockito.verify(reviewValidator, Mockito.times(1))
                    .validateReviewEligibility(engagementId, disabledId);
        }

        @Test
        @DisplayName("도우미가 조회 시 장애인 평가용 키워드 반환")
        void shouldReturnDisabledKeywords_whenHelperUserRequests() {
            // Given
            GetReviewKeywordsUseCase.Param param = new GetReviewKeywordsUseCase.Param(engagementId, helperId);

            Match mockMatch = Mockito.mock(Match.class);
            Mockito.when(mockMatch.getDisabledId()).thenReturn(disabledId);
            Mockito.when(mockMatch.getHelperId()).thenReturn(helperId);

            ReviewValidator.ValidationResult validation = new ReviewValidator.ValidationResult(mockMatch);

            Mockito.when(reviewValidator.validateReviewEligibility(engagementId, helperId))
                    .thenReturn(validation);

            // When
            GetReviewKeywordsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getKeywords()).isNotEmpty();

            Mockito.verify(reviewValidator, Mockito.times(1))
                    .validateReviewEligibility(engagementId, helperId);
        }

        @Test
        @DisplayName("긍정/부정 키워드가 모두 포함됨")
        void shouldContainBothPositiveAndNegativeKeywords() {
            // Given
            GetReviewKeywordsUseCase.Param param = createParamForDisabled();

            Match mockMatch = Mockito.mock(Match.class);
            Mockito.when(mockMatch.getDisabledId()).thenReturn(disabledId);
            Mockito.when(mockMatch.getHelperId()).thenReturn(helperId);

            ReviewValidator.ValidationResult validation = new ReviewValidator.ValidationResult(mockMatch);

            Mockito.when(reviewValidator.validateReviewEligibility(engagementId, disabledId))
                    .thenReturn(validation);

            // When
            GetReviewKeywordsUseCase.Result result = useCase.execute(param);

            // Then
            long positiveCount = result.getKeywords().stream()
                    .filter(GetReviewKeywordsUseCase.KeywordDTO::getIsPositive)
                    .count();

            long negativeCount = result.getKeywords().stream()
                    .filter(k -> !k.getIsPositive())
                    .count();

            assertThat(positiveCount).isGreaterThan(0);
            assertThat(negativeCount).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("engagementId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenEngagementIdIsNull() {
            // Given
            GetReviewKeywordsUseCase.Param param = new GetReviewKeywordsUseCase.Param(
                    null,
                    disabledId
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("engagementId");

            Mockito.verify(reviewValidator, Mockito.never())
                    .validateReviewEligibility(Mockito.anyLong(), Mockito.anyLong());
        }

        @Test
        @DisplayName("memberId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenMemberIdIsNull() {
            // Given
            GetReviewKeywordsUseCase.Param param = new GetReviewKeywordsUseCase.Param(
                    engagementId,
                    null
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("memberId");

            verify(reviewValidator, never()).validateReviewEligibility(anyLong(), anyLong());
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 실패")
    class BusinessLogicFailures {

        @Test
        @DisplayName("활동이 존재하지 않으면 MatchException 발생")
        void shouldThrowException_whenEngagementNotFound() {
            // Given
            GetReviewKeywordsUseCase.Param param = createParamForDisabled();

            when(reviewValidator.validateReviewEligibility(engagementId, disabledId))
                    .thenThrow(MatchErrors.MATCH_NOT_FOUND.toException());

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            verify(reviewValidator, times(1)).validateReviewEligibility(engagementId, disabledId);
        }

        @Test
        @DisplayName("활동 참여자가 아니면 MatchException 발생")
        void shouldThrowException_whenNotParticipant() {
            // Given
            Long nonParticipantId = 999L;
            GetReviewKeywordsUseCase.Param param = new GetReviewKeywordsUseCase.Param(
                    engagementId,
                    nonParticipantId
            );

            when(reviewValidator.validateReviewEligibility(engagementId, nonParticipantId))
                    .thenThrow(MatchErrors.NOT_ENGAGEMENT_MEMBER.toException());

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            verify(reviewValidator, times(1)).validateReviewEligibility(engagementId, nonParticipantId);
        }

        @Test
        @DisplayName("활동이 완료되지 않으면 MatchException 발생")
        void shouldThrowException_whenEngagementNotCompleted() {
            // Given
            GetReviewKeywordsUseCase.Param param = createParamForDisabled();

            when(reviewValidator.validateReviewEligibility(engagementId, disabledId))
                    .thenThrow(MatchErrors.ENGAGEMENT_NOT_COMPLETED.toException());

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            verify(reviewValidator, times(1)).validateReviewEligibility(engagementId, disabledId);
        }
    }

    // ========== 헬퍼 메서드 ==========

    private GetReviewKeywordsUseCase.Param createParamForDisabled() {
        return new GetReviewKeywordsUseCase.Param(engagementId, disabledId);
    }

    private GetReviewKeywordsUseCase.Param createParamForHelper() {
        return new GetReviewKeywordsUseCase.Param(engagementId, helperId);
    }

    private Match createMockMatch(Long disabledId, Long helperId) {
        Match match = Mockito.mock(Match.class);

        Mockito.when(match.getDisabledId()).thenReturn(disabledId);
        Mockito.when(match.getHelperId()).thenReturn(helperId);

        return match;
    }
}