package com.lgcns.bebee.match.application;

import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.ApplyHelperUseCase;
import com.lgcns.bebee.match.common.exception.MatchException;
import com.lgcns.bebee.match.domain.entity.Application;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("도우미 지원 유스케이스 테스트")
class ApplyHelperUseCaseTest {

    @Mock
    private MemberManager memberManager;

    @Mock
    private PostManager postManager;

    @Mock
    private HelperApplicationRepository applicationRepository;

    @InjectMocks
    private ApplyHelperUseCase useCase;

    private Long memberId;
    private Long postId;

    @BeforeEach
    void setUp() {
        memberId = 101L;
        postId = 1001L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("유효한 파라미터로 지원하면 Application이 생성된다")
        void shouldCreateApplication_whenAllParametersAreValid() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, false);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Post mockPost = mock(Post.class);
            when(mockPost.getStatus()).thenReturn(PostStatus.NON_MATCHED);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            when(applicationRepository.existsByApplicantIdAndPost_Id(memberId, postId)).thenReturn(false);

            Application mockApplication = mock(Application.class);
            when(applicationRepository.save(any(Application.class))).thenReturn(mockApplication);

            // When
            useCase.execute(param);

            // Then
            verify(memberManager, times(1)).findExistingMember(memberId);
            verify(postManager, times(1)).findSinglePost(postId);
            verify(applicationRepository, times(1)).existsByApplicantIdAndPost_Id(memberId, postId);
            verify(applicationRepository, times(1)).save(any(Application.class));
        }

        @Test
        @DisplayName("isVolunteer가 true일 때도 정상 처리된다")
        void shouldCreateApplication_whenIsVolunteerIsTrue() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, true);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Post mockPost = mock(Post.class);
            when(mockPost.getStatus()).thenReturn(PostStatus.NON_MATCHED);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            when(applicationRepository.existsByApplicantIdAndPost_Id(memberId, postId)).thenReturn(false);

            Application mockApplication = mock(Application.class);
            when(applicationRepository.save(any(Application.class))).thenReturn(mockApplication);

            // When
            useCase.execute(param);

            // Then
            verify(applicationRepository, times(1)).save(any(Application.class));
        }
    }

    @Nested
    @DisplayName("파라미터 검증 실패")
    class ParameterValidationFailures {

        @Test
        @DisplayName("memberId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenMemberIdIsNull() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(null, postId, false);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("helperId");
        }

        @Test
        @DisplayName("memberId가 0 이하면 InvalidParamException 발생")
        void shouldThrowException_whenMemberIdIsZeroOrNegative() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(0L, postId, false);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("helperId");
        }

        @Test
        @DisplayName("postId가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenPostIdIsNull() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, null, false);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("disabledId");
        }

        @Test
        @DisplayName("postId가 0 이하면 InvalidParamException 발생")
        void shouldThrowException_whenPostIdIsZeroOrNegative() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, -1L, false);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining("disabledId");
        }

        @Test
        @DisplayName("isVolunteer가 null이면 InvalidParamException 발생")
        void shouldThrowException_whenIsVolunteerIsNull() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, null);

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
        @DisplayName("HELPER가 아닌 회원이 지원하면 MatchException 발생")
        void shouldThrowException_whenMemberIsNotHelper() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, false);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.DISABLED); // HELPER가 아님
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessageContaining("도우미만 지원할 수 있습니다");

            verify(memberManager, times(1)).findExistingMember(memberId);
            verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        @DisplayName("이미 지원한 게시글에 중복 지원하면 MatchException 발생")
        void shouldThrowException_whenAlreadyApplied() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, false);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            when(applicationRepository.existsByApplicantIdAndPost_Id(memberId, postId)).thenReturn(true); // 이미 지원함

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessageContaining("이미 지원한 게시글입니다");

            verify(applicationRepository, times(1)).existsByApplicantIdAndPost_Id(memberId, postId);
            verify(postManager, never()).findSinglePost(anyLong());
            verify(applicationRepository, never()).save(any(Application.class));
        }

        @Test
        @DisplayName("이미 매칭된 게시글에 지원하면 MatchException 발생")
        void shouldThrowException_whenPostIsAlreadyMatched() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, postId, false);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            when(applicationRepository.existsByApplicantIdAndPost_Id(memberId, postId)).thenReturn(false);

            Post mockPost = mock(Post.class);
            when(mockPost.getStatus()).thenReturn(PostStatus.MATCHED); // 이미 매칭됨
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            // When & Then
            assertThatThrownBy(() -> useCase.execute(param))
                    .isInstanceOf(MatchException.class)
                    .hasMessageContaining("이미 매칭이 성사된 요청입니다");

            verify(postManager, times(1)).findSinglePost(postId);
            verify(applicationRepository, never()).save(any(Application.class));
        }
    }

    @Nested
    @DisplayName("경계값 테스트")
    class BoundaryValueTests {

        @Test
        @DisplayName("memberId가 1일 때 정상 처리")
        void shouldCreateApplication_whenMemberIdIsOne() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(1L, postId, false);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(1L)).thenReturn(mockMember);

            Post mockPost = mock(Post.class);
            when(mockPost.getStatus()).thenReturn(PostStatus.NON_MATCHED);
            when(postManager.findSinglePost(postId)).thenReturn(mockPost);

            when(applicationRepository.existsByApplicantIdAndPost_Id(1L, postId)).thenReturn(false);

            Application mockApplication = mock(Application.class);
            when(applicationRepository.save(any(Application.class))).thenReturn(mockApplication);

            // When
            useCase.execute(param);

            // Then
            verify(applicationRepository, times(1)).save(any(Application.class));
        }

        @Test
        @DisplayName("postId가 1일 때 정상 처리")
        void shouldCreateApplication_whenPostIdIsOne() {
            // Given
            ApplyHelperUseCase.Param param = new ApplyHelperUseCase.Param(memberId, 1L, false);

            MemberSync mockMember = mock(MemberSync.class);
            when(mockMember.getRole()).thenReturn(Role.HELPER);
            when(memberManager.findExistingMember(memberId)).thenReturn(mockMember);

            Post mockPost = mock(Post.class);
            when(mockPost.getStatus()).thenReturn(PostStatus.NON_MATCHED);
            when(postManager.findSinglePost(1L)).thenReturn(mockPost);

            when(applicationRepository.existsByApplicantIdAndPost_Id(memberId, 1L)).thenReturn(false);

            Application mockApplication = mock(Application.class);
            when(applicationRepository.save(any(Application.class))).thenReturn(mockApplication);

            // When
            useCase.execute(param);

            // Then
            verify(applicationRepository, times(1)).save(any(Application.class));
        }
    }
}