package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.GetHelperApplicationPostsUseCase;
import com.lgcns.bebee.match.common.exception.MatchException;
import com.lgcns.bebee.match.domain.entity.*;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;

import java.time.DayOfWeek;
import com.lgcns.bebee.match.domain.repository.HelperApplicationRepository;
import com.lgcns.bebee.match.domain.repository.PostRepository;
import com.lgcns.bebee.match.domain.service.MemberManager;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("본인 작성 게시글 목록 조회 유스케이스 테스트")
class GetHelperApplicationPostsUseCaseTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private HelperApplicationRepository applicationRepository;

    @Mock
    private MemberManager memberManager;

    @InjectMocks
    private GetHelperApplicationPostsUseCase useCase;

    private Long disabledMemberId;
    private Long helperMemberId;

    @BeforeEach
    void setUp() {
        disabledMemberId = 100L;
        helperMemberId = 700L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("장애인 회원이 본인 게시글 목록을 조회하면 성공한다")
        void shouldReturnPosts_whenDisabledMemberRequestsOwnPosts() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(disabledMemberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(disabledMemberId)).thenReturn(mockMember);

            Post mockPost = createMockPost(1001L, "병원 동행", EngagementType.DAY, PostStatus.NON_MATCHED);
            when(postRepository.findAllByMemberId(disabledMemberId)).thenReturn(List.of(mockPost));

            List<Application> applications = List.of(
                    createMockApplication(helperMemberId, false),
                    createMockApplication(helperMemberId + 1, true)
            );
            when(applicationRepository.findAllByPost_Id(1001L)).thenReturn(applications);

            // When
            GetHelperApplicationPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPosts()).hasSize(1);
            assertThat(result.getPosts().get(0).getPostId()).isEqualTo(1001L);
            assertThat(result.getPosts().get(0).getTitle()).isEqualTo("병원 동행");
            assertThat(result.getPosts().get(0).getCommonApplicantCount()).isEqualTo(1);
            assertThat(result.getPosts().get(0).getVolunteerApplicantCount()).isEqualTo(1);
            assertThat(result.getPosts().get(0).getIsMatched()).isFalse();

            verify(memberManager, times(1)).findExistingMember(disabledMemberId);
            verify(postRepository, times(1)).findAllByMemberId(disabledMemberId);
            verify(applicationRepository, times(1)).findAllByPost_Id(1001L);
        }

        @Test
        @DisplayName("게시글이 없으면 빈 리스트를 반환한다")
        void shouldReturnEmptyList_whenNoPostsExist() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(disabledMemberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(disabledMemberId)).thenReturn(mockMember);

            when(postRepository.findAllByMemberId(disabledMemberId)).thenReturn(Collections.emptyList());

            // When
            GetHelperApplicationPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPosts()).isEmpty();
            verify(postRepository, times(1)).findAllByMemberId(disabledMemberId);
        }

        @Test
        @DisplayName("지원자가 없는 게시글도 정상 조회된다")
        void shouldReturnPost_whenNoApplicationsExist() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(disabledMemberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(disabledMemberId)).thenReturn(mockMember);

            Post mockPost = createMockPost(1001L, "병원 동행", EngagementType.DAY, PostStatus.NON_MATCHED);
            when(postRepository.findAllByMemberId(disabledMemberId)).thenReturn(List.of(mockPost));
            when(applicationRepository.findAllByPost_Id(1001L)).thenReturn(Collections.emptyList());

            // When
            GetHelperApplicationPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPosts()).hasSize(1);
            assertThat(result.getPosts().get(0).getCommonApplicantCount()).isZero();
            assertThat(result.getPosts().get(0).getVolunteerApplicantCount()).isZero();
        }

        @Test
        @DisplayName("매칭된 게시글도 정상 조회된다")
        void shouldReturnPost_whenPostIsMatched() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(disabledMemberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(disabledMemberId)).thenReturn(mockMember);

            Post mockPost = createMockPost(1001L, "병원 동행", EngagementType.DAY, PostStatus.MATCHED);
            when(postRepository.findAllByMemberId(disabledMemberId)).thenReturn(List.of(mockPost));
            when(applicationRepository.findAllByPost_Id(1001L)).thenReturn(Collections.emptyList());

            // When
            GetHelperApplicationPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPosts()).hasSize(1);
            assertThat(result.getPosts().get(0).getIsMatched()).isTrue();
        }

        @Test
        @DisplayName("여러 게시글을 조회하면 모두 반환된다")
        void shouldReturnMultiplePosts_whenMultiplePostsExist() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(disabledMemberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(disabledMemberId)).thenReturn(mockMember);

            Post mockPost1 = createMockPost(1001L, "병원 동행", EngagementType.DAY, PostStatus.NON_MATCHED);
            Post mockPost2 = createMockPost(1002L, "마트 장보기", EngagementType.DAY, PostStatus.NON_MATCHED);
            when(postRepository.findAllByMemberId(disabledMemberId)).thenReturn(List.of(mockPost1, mockPost2));

            when(applicationRepository.findAllByPost_Id(1001L)).thenReturn(Collections.emptyList());
            when(applicationRepository.findAllByPost_Id(1002L)).thenReturn(Collections.emptyList());

            // When
            GetHelperApplicationPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPosts()).hasSize(2);
            assertThat(result.getPosts().get(0).getPostId()).isEqualTo(1001L);
            assertThat(result.getPosts().get(1).getPostId()).isEqualTo(1002L);
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("memberId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenMemberIdIsNull() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(null);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("memberId");
        }

        @Test
        @DisplayName("memberId가 0 이하면 InvalidParamException 발생")
        void shouldThrowException_whenMemberIdIsZeroOrNegative() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(0L);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("memberId");
        }
    }

    @Nested
    @DisplayName("비즈니스 로직 검증 실패")
    class BusinessLogicFailures {

        @Test
        @DisplayName("DISABLED가 아닌 회원이 조회하면 MatchException 발생")
        void shouldThrowException_whenMemberIsNotDisabled() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(helperMemberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER); // DISABLED가 아님
            when(memberManager.findExistingMember(helperMemberId)).thenReturn(mockMember);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessageContaining("장애인 회원에게 주어진 권한입니다");

            verify(memberManager, times(1)).findExistingMember(helperMemberId);
            verify(postRepository, never()).findAllByMemberId(anyLong());
        }
    }

    @Nested
    @DisplayName("지원자 카운트 계산")
    class ApplicantCountCalculation {

        @Test
        @DisplayName("유료 지원자만 있으면 commonCount만 증가한다")
        void shouldCountOnlyCommonApplicants_whenOnlyPaidApplicationsExist() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(disabledMemberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(disabledMemberId)).thenReturn(mockMember);

            Post mockPost = createMockPost(1001L, "병원 동행", EngagementType.DAY, PostStatus.NON_MATCHED);
            when(postRepository.findAllByMemberId(disabledMemberId)).thenReturn(List.of(mockPost));

            List<Application> applications = List.of(
                    createMockApplication(700L, false),
                    createMockApplication(800L, false),
                    createMockApplication(900L, false)
            );
            when(applicationRepository.findAllByPost_Id(1001L)).thenReturn(applications);

            // When
            GetHelperApplicationPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPosts().get(0).getCommonApplicantCount()).isEqualTo(3);
            assertThat(result.getPosts().get(0).getVolunteerApplicantCount()).isZero();
        }

        @Test
        @DisplayName("나눔 지원자만 있으면 volunteerCount만 증가한다")
        void shouldCountOnlyVolunteerApplicants_whenOnlyVolunteerApplicationsExist() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(disabledMemberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(disabledMemberId)).thenReturn(mockMember);

            Post mockPost = createMockPost(1001L, "병원 동행", EngagementType.DAY, PostStatus.NON_MATCHED);
            when(postRepository.findAllByMemberId(disabledMemberId)).thenReturn(List.of(mockPost));

            List<Application> applications = List.of(
                    createMockApplication(700L, true),
                    createMockApplication(800L, true)
            );
            when(applicationRepository.findAllByPost_Id(1001L)).thenReturn(applications);

            // When
            GetHelperApplicationPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPosts().get(0).getCommonApplicantCount()).isZero();
            assertThat(result.getPosts().get(0).getVolunteerApplicantCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("유료와 나눔 지원자가 섞여있으면 각각 카운트된다")
        void shouldCountBothTypes_whenMixedApplicationsExist() {
            // Given
            GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(disabledMemberId);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED);
            when(memberManager.findExistingMember(disabledMemberId)).thenReturn(mockMember);

            Post mockPost = createMockPost(1001L, "병원 동행", EngagementType.DAY, PostStatus.NON_MATCHED);
            when(postRepository.findAllByMemberId(disabledMemberId)).thenReturn(List.of(mockPost));

            List<Application> applications = List.of(
                    createMockApplication(700L, false),
                    createMockApplication(800L, true),
                    createMockApplication(900L, false),
                    createMockApplication(1000L, true),
                    createMockApplication(1100L, true)
            );
            when(applicationRepository.findAllByPost_Id(1001L)).thenReturn(applications);

            // When
            GetHelperApplicationPostsUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getPosts().get(0).getCommonApplicantCount()).isEqualTo(2);
            assertThat(result.getPosts().get(0).getVolunteerApplicantCount()).isEqualTo(3);
        }
    }

    // Helper methods
    private Post createMockPost(Long postId, String title, EngagementType type, PostStatus status) {
        Post mockPost = mock(Post.class);
        when(mockPost.getId()).thenReturn(postId);
        when(mockPost.getTitle()).thenReturn(title);
        when(mockPost.getType()).thenReturn(type);
        when(mockPost.getStatus()).thenReturn(status);
        when(mockPost.getRegion()).thenReturn("서울시 강남구 역삼동");

        PostPeriod mockPeriod = mock(PostPeriod.class);
        when(mockPeriod.getStartDate()).thenReturn(LocalDate.now());
        when(mockPeriod.getEndDate()).thenReturn(LocalDate.now().plusDays(7));
        when(mockPost.getPeriod()).thenReturn(mockPeriod);

        PostSchedule mockSchedule = mock(PostSchedule.class);
        when(mockSchedule.getDayOfWeek()).thenReturn(DayOfWeek.MONDAY);
        when(mockSchedule.getStartTime()).thenReturn(LocalTime.of(9, 0));
        when(mockSchedule.getEndTime()).thenReturn(LocalTime.of(12, 0));
        when(mockPost.getSchedules()).thenReturn(List.of(mockSchedule));

        when(mockPost.getHelpCategories()).thenReturn(new ArrayList<>());

        return mockPost;
    }

    private Application createMockApplication(Long applicantId, boolean isVolunteer) {
        Application mockApplication = mock(Application.class);
        when(mockApplication.getApplicantId()).thenReturn(applicantId);
        when(mockApplication.getIsVolunteer()).thenReturn(isVolunteer);
        return mockApplication;
    }
}