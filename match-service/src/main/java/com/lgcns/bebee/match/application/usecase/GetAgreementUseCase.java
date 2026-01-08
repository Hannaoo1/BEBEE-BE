package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.util.AgeGroupCalculator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.AgreementPeriod;
import com.lgcns.bebee.match.domain.entity.AgreementSchedule;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import com.lgcns.bebee.match.domain.service.MemberManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAgreementUseCase implements UseCase<GetAgreementUseCase.Param, GetAgreementUseCase.Result> {
    private final MemberManager memberManager;
    private final AgreementReader agreementReader;

    @Override
    public Result execute(Param params) {
        Agreement agreement = agreementReader.getById(params.agreementId);

        // otherId 결정
        Long otherId = agreement.getHelperId().equals(params.currentMemberId)
                ? agreement.getDisabledId()
                : agreement.getHelperId();

        // otherMember 조회
        MemberSync otherMember = memberManager.findExistingMember(otherId);

        return Result.from(agreement, otherMember);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long currentMemberId;
        private final Long agreementId;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result{
        private final Long agreementId;
        private final String helpType;
        private final LocalDate date;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final List<ScheduleDTO> schedules;

        private final Long unitHoney;
        private final Long totalHoney;

        private final Long otherId;
        private final String otherProfileImageUrl;
        private final String otherNickname;
        private final String otherGender;
        private final Integer otherAgeGroup;

        public static Result from(Agreement agreement, MemberSync otherMember){
            // 날짜 정보 설정
            LocalDate date = null;
            LocalDate startDate = null;
            LocalDate endDate = null;

            AgreementPeriod period = agreement.getPeriod();
            if (period != null) {
                if (agreement.getType() == EngagementType.DAY) {
                    date = period.getStartDate();
                } else if (agreement.getType() == EngagementType.TERM) {
                    startDate = period.getStartDate();
                    endDate = period.getEndDate();
                }
            }

            // schedules 변환
            List<ScheduleDTO> schedules = agreement.getSchedules().stream()
                    .sorted(Comparator.comparing(AgreementSchedule::getDayOfWeek))
                    .map(ScheduleDTO::from)
                    .collect(Collectors.toList());

            // otherMember 정보
            Integer ageGroup = AgeGroupCalculator.calculateAgeGroup(otherMember.getBirthDate());

            return new Result(
                    agreement.getId(),
                    agreement.getType().name(),
                    date,
                    startDate,
                    endDate,
                    schedules,
                    agreement.getUnitHoney(),
                    agreement.getTotalHoney(),
                    otherMember.getId(),
                    otherMember.getProfileImageUrl(),
                    otherMember.getNickname(),
                    otherMember.getGender().name(),
                    ageGroup
            );
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ScheduleDTO{
        private final String dayOfWeek;
        private final LocalTime startTime;
        private final LocalTime endTime;

        public static ScheduleDTO from(AgreementSchedule schedule) {
            return new ScheduleDTO(
                    schedule.getDayOfWeek().name(),
                    schedule.getStartTime(),
                    schedule.getEndTime()
            );
        }
    }
}
