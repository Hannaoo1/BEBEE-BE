package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchException;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.repository.ReviewRepository;
import com.lgcns.bebee.match.domain.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("리뷰 작성 유스케이스 테스트")
class CreateReviewUseCaseTest {

    @Mock
    private EngagementReader engagementReader;

    @Mock
    private AgreementReader agreementReader;

    @Mock
    private MatchReader matchReader;

    @Mock
    private MemberManager memberManager;

    @Mock
    private ReviewManager reviewManager;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private CreateReviewUseCase useCase;

    private Long engagementId;
    private Long agreementId;
    private Long matchId;
    private Long disabledId;
    private Long helperId;
    private Long reviewId;
    private List<Integer> validKeywordIds;

    @BeforeEach
    void setUp() {
        engagementId = 999888777666L;
        agreementId = 100L;
        matchId = 200L;
        disabledId = 101L;
        helperId = 202L;
        reviewId = 300L;
        validKeywordIds = List.of(1, 3, 5);  // 장애인 → 도우미 긍정 키워드
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("장애인이 도우미에게 리뷰 작성 성공 - DISABLED_TO_HELPER")
        void shouldCreateReview_whenDisabledToHelper() throws Exception {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    disabledId,
                    validKeywordIds
            );

            Engagement mockEngagement = createMockEngagement(
                    engagementId,
                    agreementId,
                    EngagementStatus.COMPLETED
            );
            Agreement mockAgreement = createMockAgreement(agreementId);
            Match mockMatch = createMockMatch(matchId, helperId, disabledId);
            MemberSync disabledMember = createMockMember(disabledId, "장애인닉네임", Role.DISABLED);
            Review mockReview = createMockReview(reviewId);

            when(engagementReader.getById(engagementId)).thenReturn(mockEngagement);
            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            when(matchReader.getByAgreementId(agreementId)).thenReturn(mockMatch);
            when(memberManager.findExistingMember(disabledId)).thenReturn(disabledMember);
            when(reviewManager.determineReviewDirection(disabledMember))
                    .thenReturn(ReviewDirection.DISABLED_TO_HELPER);
            when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
            when(mockEngagement.isLastActivity(mockAgreement)).thenReturn(true);

            doNothing().when(reviewManager).validateEngagementCompleted(mockEngagement);
            doNothing().when(reviewManager).validateLastActivity(mockEngagement, mockAgreement);
            doNothing().when(reviewManager).validateNoDuplicateReview(engagementId, disabledId);
            doNothing().when(reviewManager).validateKeywords(validKeywordIds, ReviewDirection.DISABLED_TO_HELPER);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getReviewId()).isEqualTo(reviewId);

            verify(engagementReader, times(1)).getById(engagementId);
            verify(reviewManager, times(1)).validateEngagementCompleted(mockEngagement);
            verify(reviewManager, times(1)).validateNoDuplicateReview(engagementId, disabledId);
            verify(reviewManager, times(1)).validateKeywords(validKeywordIds, ReviewDirection.DISABLED_TO_HELPER);
            verify(reviewRepository, times(1)).save(any(Review.class));
        }

        @Test
        @DisplayName("도우미가 장애인에게 리뷰 작성 성공 - HELPER_TO_DISABLED")
        void shouldCreateReview_whenHelperToDisabled() throws Exception {
            // Given
            List<Integer> helperKeywordIds = List.of(14, 16, 18);  // 도우미 → 장애인 긍정 키워드
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    helperId,
                    helperKeywordIds
            );

            Engagement mockEngagement = createMockEngagement(
                    engagementId,
                    agreementId,
                    EngagementStatus.COMPLETED
            );
            Agreement mockAgreement = createMockAgreement(agreementId);
            Match mockMatch = createMockMatch(matchId, helperId, disabledId);
            MemberSync helperMember = createMockMember(helperId, "도우미닉네임", Role.HELPER);
            Review mockReview = createMockReview(reviewId);

            when(engagementReader.getById(engagementId)).thenReturn(mockEngagement);
            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            when(matchReader.getByAgreementId(agreementId)).thenReturn(mockMatch);
            when(memberManager.findExistingMember(helperId)).thenReturn(helperMember);
            when(reviewManager.determineReviewDirection(helperMember))
                    .thenReturn(ReviewDirection.HELPER_TO_DISABLED);
            when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
            when(mockEngagement.isLastActivity(mockAgreement)).thenReturn(true);

            doNothing().when(reviewManager).validateEngagementCompleted(mockEngagement);
            doNothing().when(reviewManager).validateLastActivity(mockEngagement, mockAgreement);
            doNothing().when(reviewManager).validateNoDuplicateReview(engagementId, helperId);
            doNothing().when(reviewManager).validateKeywords(helperKeywordIds, ReviewDirection.HELPER_TO_DISABLED);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getReviewId()).isEqualTo(reviewId);

            verify(reviewManager, times(1)).validateKeywords(helperKeywordIds, ReviewDirection.HELPER_TO_DISABLED);
            verify(reviewRepository, times(1)).save(any(Review.class));
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("engagementId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenEngagementIdIsNull() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    null,
                    disabledId,
                    validKeywordIds
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("engagementId");
        }

        @Test
        @DisplayName("reviewerId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenReviewerIdIsNull() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    null,
                    validKeywordIds
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("reviewerId");
        }

        @Test
        @DisplayName("keywordIds가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenKeywordIdsIsNull() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    disabledId,
                    null
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("keywordIds");
        }

        @Test
        @DisplayName("keywordIds가 비어있으면 InvalidParamException 발생")
        void shouldThrowException_whenKeywordIdsIsEmpty() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    disabledId,
                    Collections.emptyList()
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("keywordIds");
        }

        @Test
        @DisplayName("engagementId가 0이면 InvalidParamException 발생")
        void shouldThrowException_whenEngagementIdIsZero() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    0L,
                    disabledId,
                    validKeywordIds
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("engagementId");
        }

        @Test
        @DisplayName("reviewerId가 0이면 InvalidParamException 발생")
        void shouldThrowException_whenReviewerIdIsZero() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    0L,
                    validKeywordIds
            );

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("reviewerId");
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 실패")
    class BusinessLogicFailures {

        @Test
        @DisplayName("Engagement가 존재하지 않으면 MatchException 발생")
        void shouldThrowException_whenEngagementNotFound() {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    disabledId,
                    validKeywordIds
            );

            when(engagementReader.getById(engagementId))
                    .thenThrow(MatchErrors.ENGAGEMENT_NOT_FOUND.toException());

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class);

            verify(engagementReader, times(1)).getById(engagementId);
            verify(reviewRepository, never()).save(any());
        }

        @Test
        @DisplayName("활동이 완료되지 않았으면 MatchException 발생")
        void shouldThrowException_whenEngagementNotCompleted() throws Exception {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    disabledId,
                    validKeywordIds
            );

            Engagement mockEngagement = createMockEngagement(
                    engagementId,
                    agreementId,
                    EngagementStatus.PENDING
            );
            Agreement mockAgreement = createMockAgreement(agreementId);

            when(engagementReader.getById(engagementId)).thenReturn(mockEngagement);
            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            doThrow(MatchErrors.ENGAGEMENT_NOT_COMPLETED.toException())
                    .when(reviewManager).validateEngagementCompleted(mockEngagement);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessage(MatchErrors.ENGAGEMENT_NOT_COMPLETED.getMessage());

            verify(reviewRepository, never()).save(any());
        }

        @Test
        @DisplayName("이미 리뷰를 작성했으면 MatchException 발생")
        void shouldThrowException_whenAlreadyReviewed() throws Exception {
            // Given
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    disabledId,
                    validKeywordIds
            );

            Engagement mockEngagement = createMockEngagement(
                    engagementId,
                    agreementId,
                    EngagementStatus.COMPLETED
            );
            Agreement mockAgreement = createMockAgreement(agreementId);

            when(engagementReader.getById(engagementId)).thenReturn(mockEngagement);
            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            doNothing().when(reviewManager).validateEngagementCompleted(mockEngagement);
            doNothing().when(reviewManager).validateLastActivity(mockEngagement, mockAgreement);
            doThrow(MatchErrors.ALREADY_REVIEWED.toException())
                    .when(reviewManager).validateNoDuplicateReview(engagementId, disabledId);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessage(MatchErrors.ALREADY_REVIEWED.getMessage());

            verify(reviewRepository, never()).save(any());
        }

        @Test
        @DisplayName("활동 참여자가 아니면 MatchException 발생")
        void shouldThrowException_whenNotParticipant() throws Exception {
            // Given
            Long otherId = 999L;
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    otherId,
                    validKeywordIds
            );

            Engagement mockEngagement = createMockEngagement(
                    engagementId,
                    agreementId,
                    EngagementStatus.COMPLETED
            );
            Agreement mockAgreement = createMockAgreement(agreementId);
            Match mockMatch = createMockMatch(matchId, helperId, disabledId);

            when(engagementReader.getById(engagementId)).thenReturn(mockEngagement);
            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            when(matchReader.getByAgreementId(agreementId)).thenReturn(mockMatch);
            when(mockMatch.isParticipant(otherId)).thenReturn(false);

            doNothing().when(reviewManager).validateEngagementCompleted(mockEngagement);
            doNothing().when(reviewManager).validateLastActivity(mockEngagement, mockAgreement);
            doNothing().when(reviewManager).validateNoDuplicateReview(engagementId, otherId);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessage(MatchErrors.NOT_ENGAGEMENT_MEMBER.getMessage());

            verify(reviewRepository, never()).save(any());
        }

        @Test
        @DisplayName("키워드 방향이 일치하지 않으면 MatchException 발생")
        void shouldThrowException_whenKeywordDirectionMismatch() throws Exception {
            // Given
            List<Integer> wrongKeywordIds = List.of(14, 16, 18);  // 도우미 → 장애인 키워드
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    disabledId,  // 장애인이 작성
                    wrongKeywordIds  // 하지만 도우미 키워드 사용!
            );

            Engagement mockEngagement = createMockEngagement(
                    engagementId,
                    agreementId,
                    EngagementStatus.COMPLETED
            );
            Agreement mockAgreement = createMockAgreement(agreementId);
            Match mockMatch = createMockMatch(matchId, helperId, disabledId);
            MemberSync disabledMember = createMockMember(disabledId, "장애인닉네임", Role.DISABLED);

            when(engagementReader.getById(engagementId)).thenReturn(mockEngagement);
            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            when(matchReader.getByAgreementId(agreementId)).thenReturn(mockMatch);
            when(memberManager.findExistingMember(disabledId)).thenReturn(disabledMember);
            when(reviewManager.determineReviewDirection(disabledMember))
                    .thenReturn(ReviewDirection.DISABLED_TO_HELPER);

            doNothing().when(reviewManager).validateEngagementCompleted(mockEngagement);
            doNothing().when(reviewManager).validateLastActivity(mockEngagement, mockAgreement);
            doNothing().when(reviewManager).validateNoDuplicateReview(engagementId, disabledId);
            doThrow(MatchErrors.KEYWORD_DIRECTION_MISMATCH.toException())
                    .when(reviewManager).validateKeywords(wrongKeywordIds, ReviewDirection.DISABLED_TO_HELPER);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessage(MatchErrors.KEYWORD_DIRECTION_MISMATCH.getMessage());

            verify(reviewRepository, never()).save(any());
        }

        @Test
        @DisplayName("유효하지 않은 키워드 ID면 MatchException 발생")
        void shouldThrowException_whenInvalidKeyword() throws Exception {
            // Given
            List<Integer> invalidKeywordIds = List.of(999);  // 존재하지 않는 키워드
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    disabledId,
                    invalidKeywordIds
            );

            Engagement mockEngagement = createMockEngagement(
                    engagementId,
                    agreementId,
                    EngagementStatus.COMPLETED
            );
            Agreement mockAgreement = createMockAgreement(agreementId);
            Match mockMatch = createMockMatch(matchId, helperId, disabledId);
            MemberSync disabledMember = createMockMember(disabledId, "장애인닉네임", Role.DISABLED);

            when(engagementReader.getById(engagementId)).thenReturn(mockEngagement);
            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            when(matchReader.getByAgreementId(agreementId)).thenReturn(mockMatch);
            when(memberManager.findExistingMember(disabledId)).thenReturn(disabledMember);
            when(reviewManager.determineReviewDirection(disabledMember))
                    .thenReturn(ReviewDirection.DISABLED_TO_HELPER);

            doNothing().when(reviewManager).validateEngagementCompleted(mockEngagement);
            doNothing().when(reviewManager).validateLastActivity(mockEngagement, mockAgreement);
            doNothing().when(reviewManager).validateNoDuplicateReview(engagementId, disabledId);
            doThrow(MatchErrors.INVALID_KEYWORD.toException())
                    .when(reviewManager).validateKeywords(invalidKeywordIds, ReviewDirection.DISABLED_TO_HELPER);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessage(MatchErrors.INVALID_KEYWORD.getMessage());

            verify(reviewRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("키워드 1개만 선택해도 정상 작성")
        void shouldCreateReview_withSingleKeyword() throws Exception {
            // Given
            List<Integer> singleKeyword = List.of(1);
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    disabledId,
                    singleKeyword
            );

            Engagement mockEngagement = createMockEngagement(
                    engagementId,
                    agreementId,
                    EngagementStatus.COMPLETED
            );
            Agreement mockAgreement = createMockAgreement(agreementId);
            Match mockMatch = createMockMatch(matchId, helperId, disabledId);
            MemberSync disabledMember = createMockMember(disabledId, "장애인닉네임", Role.DISABLED);
            Review mockReview = createMockReview(reviewId);

            when(engagementReader.getById(engagementId)).thenReturn(mockEngagement);
            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            when(matchReader.getByAgreementId(agreementId)).thenReturn(mockMatch);
            when(memberManager.findExistingMember(disabledId)).thenReturn(disabledMember);
            when(reviewManager.determineReviewDirection(disabledMember))
                    .thenReturn(ReviewDirection.DISABLED_TO_HELPER);
            when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
            when(mockEngagement.isLastActivity(mockAgreement)).thenReturn(true);

            doNothing().when(reviewManager).validateEngagementCompleted(mockEngagement);
            doNothing().when(reviewManager).validateLastActivity(mockEngagement, mockAgreement);
            doNothing().when(reviewManager).validateNoDuplicateReview(engagementId, disabledId);
            doNothing().when(reviewManager).validateKeywords(singleKeyword, ReviewDirection.DISABLED_TO_HELPER);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(reviewRepository, times(1)).save(any(Review.class));
        }

        @Test
        @DisplayName("모든 키워드 선택해도 정상 작성")
        void shouldCreateReview_withAllKeywords() throws Exception {
            // Given
            List<Integer> allDisabledKeywords = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
            CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                    engagementId,
                    disabledId,
                    allDisabledKeywords
            );

            Engagement mockEngagement = createMockEngagement(
                    engagementId,
                    agreementId,
                    EngagementStatus.COMPLETED
            );
            Agreement mockAgreement = createMockAgreement(agreementId);
            Match mockMatch = createMockMatch(matchId, helperId, disabledId);
            MemberSync disabledMember = createMockMember(disabledId, "장애인닉네임", Role.DISABLED);
            Review mockReview = createMockReview(reviewId);

            when(engagementReader.getById(engagementId)).thenReturn(mockEngagement);
            when(agreementReader.getById(agreementId)).thenReturn(mockAgreement);
            when(matchReader.getByAgreementId(agreementId)).thenReturn(mockMatch);
            when(memberManager.findExistingMember(disabledId)).thenReturn(disabledMember);
            when(reviewManager.determineReviewDirection(disabledMember))
                    .thenReturn(ReviewDirection.DISABLED_TO_HELPER);
            when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
            when(mockEngagement.isLastActivity(mockAgreement)).thenReturn(true);

            doNothing().when(reviewManager).validateEngagementCompleted(mockEngagement);
            doNothing().when(reviewManager).validateLastActivity(mockEngagement, mockAgreement);
            doNothing().when(reviewManager).validateNoDuplicateReview(engagementId, disabledId);
            doNothing().when(reviewManager).validateKeywords(allDisabledKeywords, ReviewDirection.DISABLED_TO_HELPER);

            // When
            CreateReviewUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            verify(reviewRepository, times(1)).save(any(Review.class));
        }
    }

    // ========== 헬퍼 메서드 ==========

    private Engagement createMockEngagement(
            Long engagementId,
            Long agreementId,
            EngagementStatus status
    ) {
        Engagement engagement = mock(Engagement.class);

        when(engagement.getEngagementId()).thenReturn(engagementId);
        when(engagement.getAgreementId()).thenReturn(agreementId);
        when(engagement.getStatus()).thenReturn(status);
        when(engagement.getType()).thenReturn(EngagementType.DAY);
        when(engagement.getActivityDate()).thenReturn(LocalDate.now());

        return engagement;
    }

    private Agreement createMockAgreement(Long agreementId) throws Exception {
        Agreement agreement = Agreement.create(
                EngagementType.DAY,
                false,
                200,
                200,
                "서울특별시 강동구",
                List.of(1L, 2L)
        );

        // Reflection으로 ID 설정
        Field idField = Agreement.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(agreement, agreementId);

        return agreement;
    }

    private Match createMockMatch(Long matchId, Long helperId, Long disabledId) {
        Match match = mock(Match.class);

        when(match.getMatchId()).thenReturn(matchId);
        when(match.getHelperId()).thenReturn(helperId);
        when(match.getDisabledId()).thenReturn(disabledId);
        when(match.isParticipant(helperId)).thenReturn(true);
        when(match.isParticipant(disabledId)).thenReturn(true);
        when(match.isParticipant(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id.equals(helperId) || id.equals(disabledId);
        });

        return match;
    }

    private MemberSync createMockMember(Long memberId, String nickname, Role role) {
        MemberSync member = mock(MemberSync.class);

        when(member.getId()).thenReturn(memberId);
        when(member.getNickname()).thenReturn(nickname);
        when(member.getRole()).thenReturn(role);

        return member;
    }

    private Review createMockReview(Long reviewId) {
        Review review = mock(Review.class);

        when(review.getId()).thenReturn(reviewId);

        return review;
    }
}