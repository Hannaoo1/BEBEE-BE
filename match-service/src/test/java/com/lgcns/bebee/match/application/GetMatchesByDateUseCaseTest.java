package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.GetMatchesByDateUseCase;
import com.lgcns.bebee.match.domain.entity.*;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.Gender;
import com.lgcns.bebee.match.domain.entity.vo.MemberRole;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import com.lgcns.bebee.match.domain.service.MemberReader;
import com.lgcns.bebee.match.domain.service.PostManager;
import com.lgcns.bebee.match.presentation.dto.DayEngagementTimeDTO;
import com.lgcns.bebee.match.presentation.dto.TermEngagementTimeDTO;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("날짜별 매칭 조회 유스케이스 테스트")
class GetMatchesByDateUseCaseTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MemberReader memberReader;

    @Mock
    private PostManager postManager;

    @InjectMocks
    private GetMatchesByDateUseCase useCase;

    private Long memberId;
    private LocalDate targetDate;
    private MatchMemberSync helperMember;
    private MatchMemberSync disabledMember;
    private Post mockPost;

    @BeforeEach
    void setUp() throws Exception {
        memberId = 101L;
        targetDate = LocalDate.of(2025, 12, 7);

        // Helper Member Mock
        helperMember = createMockMember(101L, "친절한도우미", "https://example.com/helper.jpg",
                Gender.MALE, LocalDate.of(1990, 5, 15), MemberRole.HELPER);

        // Disabled Member Mock
        disabledMember = createMockMember(202L, "김장애", "https://example.com/disabled.jpg",
                Gender.FEMALE, LocalDate.of(1960, 3, 20), MemberRole.DISABLED);

        // Post Mock
        mockPost = createMockPost(404L, "병원 동행 도우미 구해요",
                "https://example.com/posts/hospital-help.jpg");

        when(memberReader.getById(101L)).thenReturn(helperMember);
        when(memberReader.getById(202L)).thenReturn(disabledMember);
        when(postManager.findSinglePost(404L)).thenReturn(mockPost);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("DAY 타입 매칭 조회 성공")
        void shouldRetrieveDayTypeMatches() throws Exception {
            // Given
            Agreement dayAgreement = createDayAgreement(
                    1001L, 404L, 101L, 202L,
                    targetDate,
                    DayOfWeek.SUNDAY,
                    LocalTime.of(10, 0),
                    LocalTime.of(14, 0)
            );

            Match dayMatch = createMatch(3001L, 101L, 202L, 404L,
                    "병원 동행 도우미 구해요", 4001L, dayAgreement);

            when(matchRepository.findByDateAndMember(
                    eq(memberId),
                    eq(targetDate),
                    eq(DayOfWeek.SUNDAY),
                    eq(EngagementType.DAY)
            )).thenReturn(List.of(dayMatch));

            GetMatchesByDateUseCase.Param param = new GetMatchesByDateUseCase.Param(
                    memberId, targetDate, EngagementType.DAY
            );

            // When
            GetMatchesByDateUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMatchInfos()).hasSize(1);

            GetMatchesByDateUseCase.MatchInfo matchInfo = result.getMatchInfos().get(0);
            assertThat(matchInfo.getAgreementId()).isEqualTo("1001");
            assertThat(matchInfo.getPostId()).isEqualTo("404");
            assertThat(matchInfo.getTitle()).isEqualTo("병원 동행 도우미 구해요");
            assertThat(matchInfo.getType()).isEqualTo(EngagementType.DAY);
            assertThat(matchInfo.getChatRoomId()).isEqualTo("4001");

            // engagementTime 검증 (DAY 타입)
            assertThat(matchInfo.getEngagementTime()).isInstanceOf(DayEngagementTimeDTO.class);
            DayEngagementTimeDTO engagementTime = (DayEngagementTimeDTO) matchInfo.getEngagementTime();
            assertThat(engagementTime.getDate()).isEqualTo(targetDate);
            assertThat(engagementTime.getSchedule().getDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
            assertThat(engagementTime.getSchedule().getStartTime()).isEqualTo(LocalTime.of(10, 0));
            assertThat(engagementTime.getSchedule().getEndTime()).isEqualTo(LocalTime.of(14, 0));

            // Member 정보 검증
            assertThat(matchInfo.getHelper().getId()).isEqualTo("101");
            assertThat(matchInfo.getHelper().getNickname()).isEqualTo("친절한도우미");
            assertThat(matchInfo.getDisabled().getId()).isEqualTo("202");
            assertThat(matchInfo.getDisabled().getNickname()).isEqualTo("김장애");
        }

        @Test
        @DisplayName("TERM 타입 매칭 조회 성공")
        void shouldRetrieveTermTypeMatches() throws Exception {
            // Given
            LocalDate startDate = LocalDate.of(2026, 1, 1);
            LocalDate endDate = LocalDate.of(2026, 1, 31);

            Agreement termAgreement = createTermAgreement(
                    1002L, 505L, 103L, 204L,
                    startDate, endDate,
                    List.of(
                            createScheduleData(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)),
                            createScheduleData(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0))
                    )
            );

            Match termMatch = createMatch(3002L, 103L, 204L, 505L,
                    "가사 도우미 구합니다", 4002L, termAgreement);

            MatchMemberSync helper2 = createMockMember(103L, "베테랑도우미",
                    "https://example.com/helper2.jpg", Gender.FEMALE,
                    LocalDate.of(1980, 7, 10), MemberRole.HELPER);
            MatchMemberSync disabled2 = createMockMember(204L, "박장애",
                    "https://example.com/disabled2.jpg", Gender.MALE,
                    LocalDate.of(1950, 8, 25), MemberRole.DISABLED);
            Post post2 = createMockPost(505L, "가사 도우미 구합니다",
                    "https://example.com/posts/housework.jpg");

            when(memberReader.getById(103L)).thenReturn(helper2);
            when(memberReader.getById(204L)).thenReturn(disabled2);
            when(postManager.findSinglePost(505L)).thenReturn(post2);

            when(matchRepository.findByDateAndMember(
                    eq(memberId),
                    eq(targetDate),
                    any(DayOfWeek.class),
                    eq(EngagementType.TERM)
            )).thenReturn(List.of(termMatch));

            GetMatchesByDateUseCase.Param param = new GetMatchesByDateUseCase.Param(
                    memberId, targetDate, EngagementType.TERM
            );

            // When
            GetMatchesByDateUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMatchInfos()).hasSize(1);

            GetMatchesByDateUseCase.MatchInfo matchInfo = result.getMatchInfos().get(0);
            assertThat(matchInfo.getAgreementId()).isEqualTo("1002");
            assertThat(matchInfo.getType()).isEqualTo(EngagementType.TERM);

            // engagementTime 검증 (TERM 타입)
            assertThat(matchInfo.getEngagementTime()).isInstanceOf(TermEngagementTimeDTO.class);
            TermEngagementTimeDTO engagementTime = (TermEngagementTimeDTO) matchInfo.getEngagementTime();
            assertThat(engagementTime.getStartDate()).isEqualTo(startDate);
            assertThat(engagementTime.getEndDate()).isEqualTo(endDate);
            assertThat(engagementTime.getSchedules()).hasSize(2);
            assertThat(engagementTime.getSchedules().get(0).getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
            assertThat(engagementTime.getSchedules().get(1).getDayOfWeek()).isEqualTo(DayOfWeek.WEDNESDAY);
        }

        @Test
        @DisplayName("해당 날짜에 매칭이 없으면 빈 리스트 반환")
        void shouldReturnEmptyList_whenNoMatchesFound() {
            // Given
            when(matchRepository.findByDateAndMember(
                    eq(memberId),
                    eq(targetDate),
                    any(DayOfWeek.class),
                    eq(EngagementType.DAY)
            )).thenReturn(List.of());

            GetMatchesByDateUseCase.Param param = new GetMatchesByDateUseCase.Param(
                    memberId, targetDate, EngagementType.DAY
            );

            // When
            GetMatchesByDateUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMatchInfos()).isEmpty();
        }

        @Test
        @DisplayName("여러 개의 매칭 조회 성공")
        void shouldRetrieveMultipleMatches() throws Exception {
            // Given
            Agreement agreement1 = createDayAgreement(
                    1001L, 404L, 101L, 202L, targetDate,
                    DayOfWeek.SUNDAY, LocalTime.of(10, 0), LocalTime.of(14, 0)
            );
            Agreement agreement2 = createDayAgreement(
                    1003L, 406L, 101L, 202L, targetDate,
                    DayOfWeek.SUNDAY, LocalTime.of(15, 0), LocalTime.of(17, 0)
            );

            Match match1 = createMatch(3001L, 101L, 202L, 404L, "매칭1", 4001L, agreement1);
            Match match2 = createMatch(3003L, 101L, 202L, 406L, "매칭2", 4003L, agreement2);

            Post post2 = createMockPost(406L, "매칭2", "https://example.com/post2.jpg");
            when(postManager.findSinglePost(406L)).thenReturn(post2);

            when(matchRepository.findByDateAndMember(
                    eq(memberId),
                    eq(targetDate),
                    eq(DayOfWeek.SUNDAY),
                    eq(EngagementType.DAY)
            )).thenReturn(List.of(match1, match2));

            GetMatchesByDateUseCase.Param param = new GetMatchesByDateUseCase.Param(
                    memberId, targetDate, EngagementType.DAY
            );

            // When
            GetMatchesByDateUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getMatchInfos()).hasSize(2);
            assertThat(result.getMatchInfos().get(0).getAgreementId()).isEqualTo("1001");
            assertThat(result.getMatchInfos().get(1).getAgreementId()).isEqualTo("1003");
        }

        @Test
        @DisplayName("Post에 이미지가 없을 때 thumbnailImageUrl은 null")
        void shouldReturnNullThumbnail_whenPostHasNoImages() throws Exception {
            // Given
            Post postWithoutImage = createMockPost(404L, "이미지 없는 게시글", null);
            when(postManager.findSinglePost(404L)).thenReturn(postWithoutImage);

            Agreement agreement = createDayAgreement(
                    1001L, 404L, 101L, 202L, targetDate,
                    DayOfWeek.SUNDAY, LocalTime.of(10, 0), LocalTime.of(14, 0)
            );
            Match match = createMatch(3001L, 101L, 202L, 404L, "매칭", 4001L, agreement);

            when(matchRepository.findByDateAndMember(
                    eq(memberId),
                    eq(targetDate),
                    eq(DayOfWeek.SUNDAY),
                    eq(EngagementType.DAY)
            )).thenReturn(List.of(match));

            GetMatchesByDateUseCase.Param param = new GetMatchesByDateUseCase.Param(
                    memberId, targetDate, EngagementType.DAY
            );

            // When
            GetMatchesByDateUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result.getMatchInfos().get(0).getThumbnailImageUrl()).isNull();
        }
    }

    // ========== 헬퍼 메서드 ==========

    private MatchMemberSync createMockMember(Long id, String nickname, String profileImageUrl,
                                              Gender gender, LocalDate birthDate, MemberRole role) throws Exception {
        MatchMemberSync member = mock(MatchMemberSync.class);

        when(member.getId()).thenReturn(id);
        when(member.getNickname()).thenReturn(nickname);
        when(member.getProfileImageUrl()).thenReturn(profileImageUrl);
        when(member.getGender()).thenReturn(gender);
        when(member.getBirthDate()).thenReturn(birthDate);
        when(member.getRole()).thenReturn(role);

        return member;
    }

    private Post createMockPost(Long postId, String title, String imageUrl) {
        Post post = mock(Post.class);

        List<PostImage> images = new ArrayList<>();
        if (imageUrl != null) {
            PostImage image = mock(PostImage.class);
            when(image.getImageUrl()).thenReturn(imageUrl);
            images.add(image);
        }

        when(post.getId()).thenReturn(postId);
        when(post.getTitle()).thenReturn(title);
        when(post.getImages()).thenReturn(images);

        return post;
    }

    private Agreement createDayAgreement(Long agreementId, Long postId, Long helperId, Long disabledId,
                                          LocalDate date, DayOfWeek dayOfWeek,
                                          LocalTime startTime, LocalTime endTime) throws Exception {
        Agreement agreement = createAgreementInstance();

        setField(agreement, "id", agreementId);
        setField(agreement, "postId", postId);
        setField(agreement, "helperId", helperId);
        setField(agreement, "disabledId", disabledId);
        setField(agreement, "type", EngagementType.DAY);
        setField(agreement, "unitHoney", 200);
        setField(agreement, "totalHoney", 200);
        setField(agreement, "region", "서울특별시 중구 장충동");
        setField(agreement, "isVolunteer", false);
        setField(agreement, "confirmationDate", LocalDate.now());
        setField(agreement, "isDayComplete", false);
        setField(agreement, "isTermComplete", false);

        // Period 설정
        AgreementPeriod period = AgreementPeriod.create(date, date);
        setField(period, "id", 1L);
        setField(period, "agreement", agreement);
        setField(agreement, "period", period);

        // Schedule 설정
        AgreementSchedule schedule = AgreementSchedule.create(dayOfWeek, startTime, endTime);
        setField(schedule, "id", 1L);
        setField(schedule, "agreement", agreement);

        List<AgreementSchedule> schedules = new ArrayList<>();
        schedules.add(schedule);
        setField(agreement, "schedules", schedules);

        // HelpCategories 설정
        setField(agreement, "helpCategories", new ArrayList<>());

        return agreement;
    }

    private Agreement createTermAgreement(Long agreementId, Long postId, Long helperId, Long disabledId,
                                           LocalDate startDate, LocalDate endDate,
                                           List<ScheduleData> schedulesData) throws Exception {
        Agreement agreement = createAgreementInstance();

        setField(agreement, "id", agreementId);
        setField(agreement, "postId", postId);
        setField(agreement, "helperId", helperId);
        setField(agreement, "disabledId", disabledId);
        setField(agreement, "type", EngagementType.TERM);
        setField(agreement, "unitHoney", 200);
        setField(agreement, "totalHoney", 1200);
        setField(agreement, "region", "서울특별시 중구 장충동");
        setField(agreement, "isVolunteer", false);
        setField(agreement, "confirmationDate", LocalDate.now());
        setField(agreement, "isDayComplete", false);
        setField(agreement, "isTermComplete", false);

        // Period 설정
        AgreementPeriod period = AgreementPeriod.create(startDate, endDate);
        setField(period, "id", 1L);
        setField(period, "agreement", agreement);
        setField(agreement, "period", period);

        // Schedules 설정
        List<AgreementSchedule> schedules = new ArrayList<>();
        for (int i = 0; i < schedulesData.size(); i++) {
            ScheduleData data = schedulesData.get(i);
            AgreementSchedule schedule = AgreementSchedule.create(
                    data.dayOfWeek, data.startTime, data.endTime
            );
            setField(schedule, "id", (long) (i + 1));
            setField(schedule, "agreement", agreement);
            schedules.add(schedule);
        }
        setField(agreement, "schedules", schedules);

        // HelpCategories 설정
        setField(agreement, "helpCategories", new ArrayList<>());

        return agreement;
    }

    private Match createMatch(Long matchId, Long helperId, Long disabledId, Long postId,
                               String title, Long chatRoomId, Agreement agreement) throws Exception {
        Match match = Match.create(helperId, disabledId, postId, title, chatRoomId, agreement);

        setField(match, "matchId", matchId);

        return match;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = findField(target.getClass(), fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + fieldName + " not found in " + clazz.getName() + " or its superclasses");
    }

    private Agreement createAgreementInstance() throws Exception {
        Constructor<Agreement> constructor = Agreement.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private ScheduleData createScheduleData(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return new ScheduleData(dayOfWeek, startTime, endTime);
    }

    private static class ScheduleData {
        DayOfWeek dayOfWeek;
        LocalTime startTime;
        LocalTime endTime;

        ScheduleData(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
            this.dayOfWeek = dayOfWeek;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}