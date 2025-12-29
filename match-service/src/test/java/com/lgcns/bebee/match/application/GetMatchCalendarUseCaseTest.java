package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.GetMatchCalendarUseCase;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.AgreementPeriod;
import com.lgcns.bebee.match.domain.entity.AgreementSchedule;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("매칭 캘린더 조회 유스케이스 테스트")
class GetMatchCalendarUseCaseTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private GetMatchCalendarUseCase useCase;

    private Long memberId;
    private Integer year;
    private Integer month;
    private LocalDate monthStart;
    private LocalDate monthEnd;

    @BeforeEach
    void setUp() {
        memberId = 101L;
        year = 2025;
        month = 12;
        monthStart = LocalDate.of(2025, 12, 1);
        monthEnd = LocalDate.of(2025, 12, 31);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("DAY 타입 매칭만 있을 때 해당 날짜만 반환")
        void shouldReturnSingleDate_whenOnlyDayTypeMatch() throws Exception {
            // Given
            LocalDate activityDate = LocalDate.of(2025, 12, 15);
            Agreement dayAgreement = createDayAgreement(1001L, activityDate, DayOfWeek.MONDAY);
            Match dayMatch = createMatch(3001L, dayAgreement);

            when(matchRepository.findByMonthAndMember(eq(memberId), eq(monthStart), eq(monthEnd)))
                    .thenReturn(List.of(dayMatch));

            GetMatchCalendarUseCase.Param param = new GetMatchCalendarUseCase.Param(memberId, year, month);

            // When
            GetMatchCalendarUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getActiveDates()).hasSize(1);
            assertThat(result.getActiveDates().get(0)).isEqualTo(activityDate);
        }

        @Test
        @DisplayName("TERM 타입 매칭의 활동 날짜들이 올바르게 계산됨")
        void shouldCalculateAllActivityDates_whenTermTypeMatch() throws Exception {
            // Given: 12월 1-31일, 월/수/금 활동
            Agreement termAgreement = createTermAgreement(
                    1002L,
                    LocalDate.of(2025, 12, 1),
                    LocalDate.of(2025, 12, 31),
                    List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)
            );
            Match termMatch = createMatch(3002L, termAgreement);

            when(matchRepository.findByMonthAndMember(eq(memberId), eq(monthStart), eq(monthEnd)))
                    .thenReturn(List.of(termMatch));

            GetMatchCalendarUseCase.Param param = new GetMatchCalendarUseCase.Param(memberId, year, month);

            // When
            GetMatchCalendarUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            List<LocalDate> activeDates = result.getActiveDates();

            // 12월 월요일: 1, 8, 15, 22, 29 (5개)
            // 12월 수요일: 3, 10, 17, 24, 31 (5개)
            // 12월 금요일: 5, 12, 19, 26 (4개)
            assertThat(activeDates).hasSize(14);
            assertThat(activeDates).contains(
                    LocalDate.of(2025, 12, 1),   // Mon
                    LocalDate.of(2025, 12, 3),   // Wed
                    LocalDate.of(2025, 12, 5),   // Fri
                    LocalDate.of(2025, 12, 8),   // Mon
                    LocalDate.of(2025, 12, 29),  // Mon
                    LocalDate.of(2025, 12, 31)   // Wed
            );

            // 날짜가 정렬되어 있는지 확인
            for (int i = 0; i < activeDates.size() - 1; i++) {
                assertThat(activeDates.get(i)).isBefore(activeDates.get(i + 1));
            }
        }

        @Test
        @DisplayName("DAY와 TERM 타입이 섞여 있을 때 모든 활동 날짜 반환")
        void shouldReturnAllDates_whenMixedDayAndTermTypes() throws Exception {
            // Given
            LocalDate dayDate = LocalDate.of(2025, 12, 25);
            Agreement dayAgreement = createDayAgreement(1001L, dayDate, DayOfWeek.THURSDAY);

            Agreement termAgreement = createTermAgreement(
                    1002L,
                    LocalDate.of(2025, 12, 1),
                    LocalDate.of(2025, 12, 10),
                    List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)
            );

            Match dayMatch = createMatch(3001L, dayAgreement);
            Match termMatch = createMatch(3002L, termAgreement);

            when(matchRepository.findByMonthAndMember(eq(memberId), eq(monthStart), eq(monthEnd)))
                    .thenReturn(List.of(dayMatch, termMatch));

            GetMatchCalendarUseCase.Param param = new GetMatchCalendarUseCase.Param(memberId, year, month);

            // When
            GetMatchCalendarUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            List<LocalDate> activeDates = result.getActiveDates();

            // TERM: 12/2(화), 12/4(목), 12/9(화) = 3개
            // DAY: 12/25(목) = 1개
            // 총 4개 (12/4와 별개)
            assertThat(activeDates).contains(
                    LocalDate.of(2025, 12, 2),   // TERM - Tue
                    LocalDate.of(2025, 12, 4),   // TERM - Thu
                    LocalDate.of(2025, 12, 9),   // TERM - Tue
                    LocalDate.of(2025, 12, 25)   // DAY - Thu
            );
        }

        @Test
        @DisplayName("TERM이 월 범위를 초과할 때 해당 월에 포함된 날짜만 반환")
        void shouldReturnOnlyDatesInMonth_whenTermSpansBeyondMonth() throws Exception {
            // Given: 11월 20일 ~ 1월 10일 기간, 월요일 활동
            Agreement termAgreement = createTermAgreement(
                    1002L,
                    LocalDate.of(2025, 11, 20),
                    LocalDate.of(2026, 1, 10),
                    List.of(DayOfWeek.MONDAY)
            );
            Match termMatch = createMatch(3002L, termAgreement);

            when(matchRepository.findByMonthAndMember(eq(memberId), eq(monthStart), eq(monthEnd)))
                    .thenReturn(List.of(termMatch));

            GetMatchCalendarUseCase.Param param = new GetMatchCalendarUseCase.Param(memberId, year, month);

            // When
            GetMatchCalendarUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            List<LocalDate> activeDates = result.getActiveDates();

            // 12월의 월요일만: 1, 8, 15, 22, 29
            assertThat(activeDates).hasSize(5);
            assertThat(activeDates).allMatch(date ->
                    date.getYear() == 2025 &&
                            date.getMonthValue() == 12 &&
                            date.getDayOfWeek() == DayOfWeek.MONDAY
            );
        }

        @Test
        @DisplayName("매칭이 없으면 빈 리스트 반환")
        void shouldReturnEmptyList_whenNoMatches() {
            // Given
            when(matchRepository.findByMonthAndMember(eq(memberId), eq(monthStart), eq(monthEnd)))
                    .thenReturn(List.of());

            GetMatchCalendarUseCase.Param param = new GetMatchCalendarUseCase.Param(memberId, year, month);

            // When
            GetMatchCalendarUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getActiveDates()).isEmpty();
        }

        @Test
        @DisplayName("같은 날짜에 여러 매칭이 있어도 중복 제거되어 반환")
        void shouldRemoveDuplicates_whenMultipleMatchesOnSameDate() throws Exception {
            // Given: 같은 날짜(12/15)에 두 개의 DAY 매칭
            LocalDate sameDate = LocalDate.of(2025, 12, 15);
            Agreement agreement1 = createDayAgreement(1001L, sameDate, DayOfWeek.MONDAY);
            Agreement agreement2 = createDayAgreement(1002L, sameDate, DayOfWeek.MONDAY);

            Match match1 = createMatch(3001L, agreement1);
            Match match2 = createMatch(3002L, agreement2);

            when(matchRepository.findByMonthAndMember(eq(memberId), eq(monthStart), eq(monthEnd)))
                    .thenReturn(List.of(match1, match2));

            GetMatchCalendarUseCase.Param param = new GetMatchCalendarUseCase.Param(memberId, year, month);

            // When
            GetMatchCalendarUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getActiveDates()).hasSize(1);
            assertThat(result.getActiveDates().get(0)).isEqualTo(sameDate);
        }

        @Test
        @DisplayName("2월(28일)의 TERM 매칭도 올바르게 처리")
        void shouldHandleFebruaryCorrectly() throws Exception {
            // Given: 2025년 2월 (28일까지)
            LocalDate febStart = LocalDate.of(2025, 2, 1);
            LocalDate febEnd = LocalDate.of(2025, 2, 28);

            Agreement termAgreement = createTermAgreement(
                    1002L,
                    febStart,
                    febEnd,
                    List.of(DayOfWeek.SATURDAY)
            );
            Match termMatch = createMatch(3002L, termAgreement);

            when(matchRepository.findByMonthAndMember(eq(memberId), eq(febStart), eq(febEnd)))
                    .thenReturn(List.of(termMatch));

            GetMatchCalendarUseCase.Param param = new GetMatchCalendarUseCase.Param(memberId, 2025, 2);

            // When
            GetMatchCalendarUseCase.Result result = useCase.execute(param);

            // Then
            assertThat(result).isNotNull();
            List<LocalDate> activeDates = result.getActiveDates();

            // 2월 토요일: 1, 8, 15, 22
            assertThat(activeDates).hasSize(4);
            assertThat(activeDates).allMatch(date ->
                    date.getYear() == 2025 &&
                            date.getMonthValue() == 2 &&
                            date.getDayOfWeek() == DayOfWeek.SATURDAY
            );
        }
    }

    // ========== 헬퍼 메서드 ==========

    private Agreement createDayAgreement(Long agreementId, LocalDate date, DayOfWeek dayOfWeek) throws Exception {
        Agreement agreement = createAgreementInstance();

        setField(agreement, "id", agreementId);
        setField(agreement, "type", EngagementType.DAY);
        setField(agreement, "helpCategories", new ArrayList<>());

        AgreementPeriod period = AgreementPeriod.create(date, date);
        setField(period, "id", 1L);
        setField(period, "agreement", agreement);
        setField(agreement, "period", period);

        AgreementSchedule schedule = AgreementSchedule.create(
                dayOfWeek,
                LocalDate.now().atTime(10, 0).toLocalTime(),
                LocalDate.now().atTime(12, 0).toLocalTime()
        );
        setField(schedule, "id", 1L);
        setField(schedule, "agreement", agreement);

        List<AgreementSchedule> schedules = new ArrayList<>();
        schedules.add(schedule);
        setField(agreement, "schedules", schedules);

        return agreement;
    }

    private Agreement createTermAgreement(Long agreementId, LocalDate startDate, LocalDate endDate,
                                           List<DayOfWeek> activityDays) throws Exception {
        Agreement agreement = createAgreementInstance();

        setField(agreement, "id", agreementId);
        setField(agreement, "type", EngagementType.TERM);
        setField(agreement, "helpCategories", new ArrayList<>());

        AgreementPeriod period = AgreementPeriod.create(startDate, endDate);
        setField(period, "id", 1L);
        setField(period, "agreement", agreement);
        setField(agreement, "period", period);

        List<AgreementSchedule> schedules = new ArrayList<>();
        for (int i = 0; i < activityDays.size(); i++) {
            AgreementSchedule schedule = AgreementSchedule.create(
                    activityDays.get(i),
                    LocalDate.now().atTime(14, 0).toLocalTime(),
                    LocalDate.now().atTime(16, 0).toLocalTime()
            );
            setField(schedule, "id", (long) (i + 1));
            setField(schedule, "agreement", agreement);
            schedules.add(schedule);
        }
        setField(agreement, "schedules", schedules);

        return agreement;
    }

    private Match createMatch(Long matchId, Agreement agreement) throws Exception {
        Match match = Match.create(
                101L,  // helperId
                202L,  // disabledId
                404L,  // postId
                "테스트 매칭",
                303L,  // chatRoomId
                agreement
        );

        setField(match, "matchId", matchId);

        return match;
    }

    private Agreement createAgreementInstance() throws Exception {
        Constructor<Agreement> constructor = Agreement.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
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
}