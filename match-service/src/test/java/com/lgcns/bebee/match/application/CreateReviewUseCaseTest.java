package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchException;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.repository.ReviewRepository;
import com.lgcns.bebee.match.domain.service.ReviewManager;
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
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("리뷰 작성 유스케이스 테스트")
class CreateReviewUseCaseTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewManager reviewManager;

    @Mock
    private ReviewValidator reviewValidator;

    @InjectMocks
    private CreateReviewUseCase useCase;

    private Long engagementId;
    private Long disabledId;
    private Long helperId;
    private Long reviewId;
    private List<Integer> keywordIds;

    @BeforeEach
    void setUp() {
        engagementId = 79L;
        disabledId = 100L;
        helperId = 200L;
        reviewId = 9L;
        keywordIds = List.of(1, 2, 3, 5, 7);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("장애인이 도우미 평가 리뷰 작성 성공")
        void shouldCreateReview_whenDisabledReviewsHelper() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(engagementId, disabledId, keywordIds);

            Match mockMatch = Mockito.mock(Match.class);
            Mockito.when(mockMatch.getDisabledId()).thenReturn(disabledId);
            Mockito.when(mockMatch.getHelperId()).thenReturn(helperId);

            ReviewValidator.ValidationResult validation = new ReviewValidator.ValidationResult(mockMatch);
            Review mockReview = Mockito.mock(Review.class);
            Mockito.when(mockReview.getId()).thenReturn(reviewId);

            Mockito.when(reviewValidator.validateReviewEligibility(engagementId, disabledId))
                    .thenReturn(validation);
            Mockito.when(reviewRepository.existsByEngagementIdAndReviewerId(engagementId, disabledId))
                    .thenReturn(false);
            Mockito.when(reviewManager.createReview(engagementId, disabledId, helperId, keywordIds))
                    .thenReturn(mockReview);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getReviewId()).isEqualTo(reviewId);

            Mockito.verify(reviewValidator, Mockito.times(1))
                    .validateReviewEligibility(engagementId, disabledId);
            Mockito.verify(reviewRepository, Mockito.times(1))
                    .existsByEngagementIdAndReviewerId(engagementId, disabledId);
            Mockito.verify(reviewManager, Mockito.times(1))
                    .createReview(engagementId, disabledId, helperId, keywordIds);
        }

        @Test
        @DisplayName("도우미가 장애인 평가 리뷰 작성 성공")
        void shouldCreateReview_whenHelperReviewsDisabled() {
            // Given
            List<Integer> helperKeywords = List.of(14, 15, 16, 17);
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(engagementId, helperId, helperKeywords);

            Match mockMatch = Mockito.mock(Match.class);
            Mockito.when(mockMatch.getDisabledId()).thenReturn(disabledId);
            Mockito.when(mockMatch.getHelperId()).thenReturn(helperId);

            ReviewValidator.ValidationResult validation = new ReviewValidator.ValidationResult(mockMatch);
            Review mockReview = Mockito.mock(Review.class);
            Mockito.when(mockReview.getId()).thenReturn(reviewId);

            Mockito.when(reviewValidator.validateReviewEligibility(engagementId, helperId))
                    .thenReturn(validation);
            Mockito.when(reviewRepository.existsByEngagementIdAndReviewerId(engagementId, helperId))
                    .thenReturn(false);
            Mockito.when(reviewManager.createReview(engagementId, helperId, disabledId, helperKeywords))
                    .thenReturn(mockReview);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getReviewId()).isEqualTo(reviewId);

            Mockito.verify(reviewManager, Mockito.times(1))
                    .createReview(engagementId, helperId, disabledId, helperKeywords);
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("engagementId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenEngagementIdIsNull() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(null, disabledId, keywordIds);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("engagementId");

            Mockito.verify(reviewValidator, Mockito.never())
                    .validateReviewEligibility(Mockito.anyLong(), Mockito.anyLong());
            Mockito.verify(reviewManager, Mockito.never())
                    .createReview(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyList());
        }

        @Test
        @DisplayName("reviewerId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenReviewerIdIsNull() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(engagementId, null, keywordIds);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("reviewerId");

            Mockito.verify(reviewValidator, Mockito.never())
                    .validateReviewEligibility(Mockito.anyLong(), Mockito.anyLong());
        }

        @Test
        @DisplayName("keywordIds가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenKeywordIdsIsNull() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(engagementId, disabledId, null);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("keywordIds");

            Mockito.verify(reviewValidator, Mockito.never())
                    .validateReviewEligibility(Mockito.anyLong(), Mockito.anyLong());
        }

        @Test
        @DisplayName("keywordIds가 빈 리스트면 InvalidParamException 발생")
        void shouldThrowException_whenKeywordIdsIsEmpty() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(engagementId, disabledId, List.of());

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("keywordIds");

            Mockito.verify(reviewValidator, Mockito.never())
                    .validateReviewEligibility(Mockito.anyLong(), Mockito.anyLong());
        }

        @Test
        @DisplayName("keywordIds가 10개를 초과하면 InvalidParamException 발생")
        void shouldThrowException_whenKeywordIdsExceeds10() {
            // Given
            List<Integer> elevenKeywords = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(engagementId, disabledId, elevenKeywords);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("keywordIds");

            Mockito.verify(reviewValidator, Mockito.never())
                    .validateReviewEligibility(Mockito.anyLong(), Mockito.anyLong());
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 실패")
    class BusinessLogicFailures {

        @Test
        @DisplayName("활동이 존재하지 않으면 MatchException 발생")
        void shouldThrowException_whenEngagementNotFound() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(engagementId, disabledId, keywordIds);

            Mockito.when(reviewValidator.validateReviewEligibility(engagementId, disabledId))
                    .thenThrow(MatchErrors.MATCH_NOT_FOUND.toException());

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            Mockito.verify(reviewValidator, Mockito.times(1))
                    .validateReviewEligibility(engagementId, disabledId);
            Mockito.verify(reviewRepository, Mockito.never())
                    .existsByEngagementIdAndReviewerId(Mockito.anyLong(), Mockito.anyLong());
            Mockito.verify(reviewManager, Mockito.never())
                    .createReview(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyList());
        }

        @Test
        @DisplayName("이미 리뷰를 작성했으면 MatchException 발생")
        void shouldThrowException_whenAlreadyReviewed() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(engagementId, disabledId, keywordIds);

            Match mockMatch = Mockito.mock(Match.class);
            Mockito.when(mockMatch.getDisabledId()).thenReturn(disabledId);
            Mockito.when(mockMatch.getHelperId()).thenReturn(helperId);

            ReviewValidator.ValidationResult validation = new ReviewValidator.ValidationResult(mockMatch);

            Mockito.when(reviewValidator.validateReviewEligibility(engagementId, disabledId))
                    .thenReturn(validation);
            Mockito.when(reviewRepository.existsByEngagementIdAndReviewerId(engagementId, disabledId))
                    .thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            Mockito.verify(reviewRepository, Mockito.times(1))
                    .existsByEngagementIdAndReviewerId(engagementId, disabledId);
            Mockito.verify(reviewManager, Mockito.never())
                    .createReview(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyList());
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("키워드 1개만 선택해도 성공")
        void shouldCreateReview_withSingleKeyword() {
            // Given
            List<Integer> singleKeyword = List.of(1);
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(engagementId, disabledId, singleKeyword);

            Match mockMatch = Mockito.mock(Match.class);
            Mockito.when(mockMatch.getDisabledId()).thenReturn(disabledId);
            Mockito.when(mockMatch.getHelperId()).thenReturn(helperId);

            ReviewValidator.ValidationResult validation = new ReviewValidator.ValidationResult(mockMatch);
            Review mockReview = Mockito.mock(Review.class);
            Mockito.when(mockReview.getId()).thenReturn(reviewId);

            Mockito.when(reviewValidator.validateReviewEligibility(engagementId, disabledId))
                    .thenReturn(validation);
            Mockito.when(reviewRepository.existsByEngagementIdAndReviewerId(engagementId, disabledId))
                    .thenReturn(false);
            Mockito.when(reviewManager.createReview(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyList()))
                    .thenReturn(mockReview);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            Mockito.verify(reviewManager, Mockito.times(1))
                    .createReview(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.eq(singleKeyword));
        }

        @Test
        @DisplayName("키워드 10개 선택해도 성공 (최대값)")
        void shouldCreateReview_with10Keywords() {
            // Given
            List<Integer> tenKeywords = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(engagementId, disabledId, tenKeywords);

            Match mockMatch = Mockito.mock(Match.class);
            Mockito.when(mockMatch.getDisabledId()).thenReturn(disabledId);
            Mockito.when(mockMatch.getHelperId()).thenReturn(helperId);

            ReviewValidator.ValidationResult validation = new ReviewValidator.ValidationResult(mockMatch);
            Review mockReview = Mockito.mock(Review.class);
            Mockito.when(mockReview.getId()).thenReturn(reviewId);

            Mockito.when(reviewValidator.validateReviewEligibility(engagementId, disabledId))
                    .thenReturn(validation);
            Mockito.when(reviewRepository.existsByEngagementIdAndReviewerId(engagementId, disabledId))
                    .thenReturn(false);
            Mockito.when(reviewManager.createReview(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyList()))
                    .thenReturn(mockReview);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            Mockito.verify(reviewManager, Mockito.times(1))
                    .createReview(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.eq(tenKeywords));
        }
    }
}