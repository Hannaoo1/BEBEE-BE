package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.GetHelperApplicationsByPostUseCase;
import com.lgcns.bebee.match.common.exception.MatchException;
import com.lgcns.bebee.match.domain.entity.Application;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.sync.Gender;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.repository.HelperApplicationRepository;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.PostManager;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("특정 게시글의 지원자 목록 조회 유스케이스 테스트")
class GetHelperApplicationsByPostUseCaseTest {

    @Mock
    private PostManager postManager;

    @Mock
    private HelperApplicationRepository applicationRepository;

    @Mock
    private MemberManager memberManager;

    @InjectMocks
    private GetHelperApplicationsByPostUseCase useCase;

    private Long disabledMemberId;
    private Long otherDisabledMemberId;
    private Long postId;

    @BeforeEach
    void setUp() {
        disabledMemberId = 100L;
        otherDisabledMemberId = 200L;
        postId = 1001L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("본인이 작성한 게시글의 지원자를 조회하면 성공한다")
        void shouldReturnApplicants_whenOwnerRequestsApplicants() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, postId);

            Post mockPost = mock(Post.class);
            when(mockPost.getMemberId()).thenReturn(disabledMemberId);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            Application app1 = createMockApplication(700L, false);
            Application app2 = createMockApplication(800L, true);
            when(applicationRepository.findAllByPost_Id(postId)).thenReturn(List.of(app1, app2));

            MemberSync helper1 = createMockMember(700L, "강지훈", Gender.MALE, LocalDate.of(1990, 5, 15));
            MemberSync helper2 = createMockMember(800L, "윤서연", Gender.FEMALE, LocalDate.of(1995, 8, 20));
            when(memberManager.findExistingMember(700L)).thenReturn(helper1);
            when(memberManager.findExistingMember(800L)).thenReturn(helper2);

            // When
            GetHelperApplicationsByPostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getApplicants()).hasSize(2);
            assertThat(result.getApplicants().get(0).getMemberId()).isEqualTo(700L);
            assertThat(result.getApplicants().get(0).getNickname()).isEqualTo("강지훈");
            assertThat(result.getApplicants().get(0).getGender()).isEqualTo(Gender.MALE);
            assertThat(result.getApplicants().get(0).getIsVolunteer()).isFalse();
            assertThat(result.getApplicants().get(1).getMemberId()).isEqualTo(800L);
            assertThat(result.getApplicants().get(1).getNickname()).isEqualTo("윤서연");
            assertThat(result.getApplicants().get(1).getIsVolunteer()).isTrue();

            verify(postManager, times(1)).findSinglePost(postId);
            verify(applicationRepository, times(1)).findAllByPost_Id(postId);
            verify(memberManager, times(1)).findExistingMember(700L);
            verify(memberManager, times(1)).findExistingMember(800L);
        }

        @Test
        @DisplayName("지원자가 없으면 빈 리스트를 반환한다")
        void shouldReturnEmptyList_whenNoApplicantsExist() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, postId);

            Post mockPost = mock(Post.class);
            when(mockPost.getMemberId()).thenReturn(disabledMemberId);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            when(applicationRepository.findAllByPost_Id(postId)).thenReturn(Collections.emptyList());

            // When
            GetHelperApplicationsByPostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getApplicants()).isEmpty();
            verify(postManager, times(1)).findSinglePost(postId);
            verify(applicationRepository, times(1)).findAllByPost_Id(postId);
        }

        @Test
        @DisplayName("지원자가 한 명만 있어도 정상 조회된다")
        void shouldReturnSingleApplicant_whenOnlyOneApplicantExists() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, postId);

            Post mockPost = mock(Post.class);
            when(mockPost.getMemberId()).thenReturn(disabledMemberId);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            Application app = createMockApplication(700L, false);
            when(applicationRepository.findAllByPost_Id(postId)).thenReturn(List.of(app));

            MemberSync helper = createMockMember(700L, "강지훈", Gender.MALE, LocalDate.of(1990, 5, 15));
            when(memberManager.findExistingMember(700L)).thenReturn(helper);

            // When
            GetHelperApplicationsByPostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getApplicants()).hasSize(1);
            assertThat(result.getApplicants().get(0).getMemberId()).isEqualTo(700L);
        }

        @Test
        @DisplayName("지원자가 여러 명이면 모두 조회된다")
        void shouldReturnAllApplicants_whenMultipleApplicantsExist() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, postId);

            Post mockPost = mock(Post.class);
            when(mockPost.getMemberId()).thenReturn(disabledMemberId);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            Application app1 = createMockApplication(700L, false);
            Application app2 = createMockApplication(800L, true);
            Application app3 = createMockApplication(900L, false);
            Application app4 = createMockApplication(1000L, true);
            when(applicationRepository.findAllByPost_Id(postId)).thenReturn(List.of(app1, app2, app3, app4));

            MemberSync helper1 = createMockMember(700L, "강지훈", Gender.MALE, LocalDate.of(1990, 5, 15));
            MemberSync helper2 = createMockMember(800L, "윤서연", Gender.FEMALE, LocalDate.of(1995, 8, 20));
            MemberSync helper3 = createMockMember(900L, "임동현", Gender.MALE, LocalDate.of(1988, 3, 10));
            MemberSync helper4 = createMockMember(1000L, "한미래", Gender.FEMALE, LocalDate.of(1992, 11, 25));

            when(memberManager.findExistingMember(700L)).thenReturn(helper1);
            when(memberManager.findExistingMember(800L)).thenReturn(helper2);
            when(memberManager.findExistingMember(900L)).thenReturn(helper3);
            when(memberManager.findExistingMember(1000L)).thenReturn(helper4);

            // When
            GetHelperApplicationsByPostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getApplicants()).hasSize(4);
            assertThat(result.getApplicants().get(0).getMemberId()).isEqualTo(700L);
            assertThat(result.getApplicants().get(1).getMemberId()).isEqualTo(800L);
            assertThat(result.getApplicants().get(2).getMemberId()).isEqualTo(900L);
            assertThat(result.getApplicants().get(3).getMemberId()).isEqualTo(1000L);
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("memberId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenMemberIdIsNull() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(null, postId);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("memberId");
        }

        @Test
        @DisplayName("memberId가 0 이하면 InvalidParamException 발생")
        void shouldThrowException_whenMemberIdIsZeroOrNegative() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(0L, postId);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("memberId");
        }

        @Test
        @DisplayName("postId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenPostIdIsNull() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, null);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("postId");
        }

        @Test
        @DisplayName("postId가 0 이하면 InvalidParamException 발생")
        void shouldThrowException_whenPostIdIsZeroOrNegative() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, -1L);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("postId");
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 검증 실패")
    class BusinessLogicFailures {

        @Test
        @DisplayName("본인이 작성한 게시글이 아니면 MatchException 발생")
        void shouldThrowException_whenNotPostOwner() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, postId);

            Post mockPost = mock(Post.class);
            when(mockPost.getMemberId()).thenReturn(otherDisabledMemberId); // 다른 사람의 게시글
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessageContaining("권한이 없습니다");

            verify(postManager, times(1)).findSinglePost(postId);
            verify(applicationRepository, never()).findAllByPost_Id(anyLong());
        }
    }

    @Nested
    @DisplayName("지원자 정보 매핑")
    class ApplicantInfoMapping {

        @Test
        @DisplayName("지원자의 연령대가 올바르게 계산된다")
        void shouldCalculateAgeGroupCorrectly() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, postId);

            Post mockPost = mock(Post.class);
            when(mockPost.getMemberId()).thenReturn(disabledMemberId);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            Application app = createMockApplication(700L, false);
            when(applicationRepository.findAllByPost_Id(postId)).thenReturn(List.of(app));

            // 1990년생 (30대)
            MemberSync helper = createMockMember(700L, "강지훈", Gender.MALE, LocalDate.of(1990, 5, 15));
            when(memberManager.findExistingMember(700L)).thenReturn(helper);

            // When
            GetHelperApplicationsByPostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getApplicants().get(0).getAgeGroup()).isEqualTo(30);
        }

        @Test
        @DisplayName("지원자의 성별이 올바르게 매핑된다")
        void shouldMapGenderCorrectly() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, postId);

            Post mockPost = mock(Post.class);
            when(mockPost.getMemberId()).thenReturn(disabledMemberId);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            Application app1 = createMockApplication(700L, false);
            Application app2 = createMockApplication(800L, false);
            when(applicationRepository.findAllByPost_Id(postId)).thenReturn(List.of(app1, app2));

            MemberSync maleHelper = createMockMember(700L, "강지훈", Gender.MALE, LocalDate.of(1990, 5, 15));
            MemberSync femaleHelper = createMockMember(800L, "윤서연", Gender.FEMALE, LocalDate.of(1995, 8, 20));
            when(memberManager.findExistingMember(700L)).thenReturn(maleHelper);
            when(memberManager.findExistingMember(800L)).thenReturn(femaleHelper);

            // When
            GetHelperApplicationsByPostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getApplicants().get(0).getGender()).isEqualTo(Gender.MALE);
            assertThat(result.getApplicants().get(1).getGender()).isEqualTo(Gender.FEMALE);
        }

        @Test
        @DisplayName("나눔 여부가 올바르게 매핑된다")
        void shouldMapIsVolunteerCorrectly() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, postId);

            Post mockPost = mock(Post.class);
            when(mockPost.getMemberId()).thenReturn(disabledMemberId);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            Application paidApp = createMockApplication(700L, false);
            Application volunteerApp = createMockApplication(800L, true);
            when(applicationRepository.findAllByPost_Id(postId)).thenReturn(List.of(paidApp, volunteerApp));

            MemberSync paidHelper = createMockMember(700L, "강지훈", Gender.MALE, LocalDate.of(1990, 5, 15));
            MemberSync volunteerHelper = createMockMember(800L, "윤서연", Gender.FEMALE, LocalDate.of(1995, 8, 20));
            when(memberManager.findExistingMember(700L)).thenReturn(paidHelper);
            when(memberManager.findExistingMember(800L)).thenReturn(volunteerHelper);

            // When
            GetHelperApplicationsByPostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getApplicants().get(0).getIsVolunteer()).isFalse();
            assertThat(result.getApplicants().get(1).getIsVolunteer()).isTrue();
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("memberId가 1일 때 정상 처리")
        void shouldWork_whenMemberIdIsOne() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(1L, postId);

            Post mockPost = mock(Post.class);
            when(mockPost.getMemberId()).thenReturn(1L);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            when(applicationRepository.findAllByPost_Id(postId)).thenReturn(Collections.emptyList());

            // When
            GetHelperApplicationsByPostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getApplicants()).isEmpty();
        }

        @Test
        @DisplayName("postId가 1일 때 정상 처리")
        void shouldWork_whenPostIdIsOne() {
            // Given
            GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                    disabledMemberId, 1L);

            Post mockPost = mock(Post.class);
            when(mockPost.getMemberId()).thenReturn(disabledMemberId);
            when(postManager.findSinglePost(1L)).thenReturn(mockPost);

            when(applicationRepository.findAllByPost_Id(1L)).thenReturn(Collections.emptyList());

            // When
            GetHelperApplicationsByPostUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getApplicants()).isEmpty();
        }
    }

    // Helper methods
    private Application createMockApplication(Long applicantId, boolean isVolunteer) {
        Application mockApplication = mock(Application.class);
        when(mockApplication.getApplicantId()).thenReturn(applicantId);
        when(mockApplication.getIsVolunteer()).thenReturn(isVolunteer);
        return mockApplication;
    }

    private MemberSync createMockMember(Long memberId, String nickname, Gender gender, LocalDate birthDate) {
        MemberSync mockMember = mock(MemberSync.class);
        when(mockMember.getId()).thenReturn(memberId);
        when(mockMember.getNickname()).thenReturn(nickname);
        when(mockMember.getGender()).thenReturn(gender);
        when(mockMember.getBirthDate()).thenReturn(birthDate);
        return mockMember;
    }
}