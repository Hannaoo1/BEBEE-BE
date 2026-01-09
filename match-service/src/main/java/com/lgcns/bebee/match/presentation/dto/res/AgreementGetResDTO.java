package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetAgreementUseCase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AgreementGetResDTO(
        String agreementId,
        String helpType,
        LocalDate date,
        LocalDate startDate,
        LocalDate endDate,
        List<ScheduleDTO> schedules,
        Integer unitHoney,
        Integer totalHoney,
        String otherId,
        String otherProfileImageUrl,
        String otherNickname,
        String otherGender,
        Integer otherAgeGroup
) {
    public static AgreementGetResDTO from(GetAgreementUseCase.Result result) {
        return new AgreementGetResDTO(
                String.valueOf(result.getAgreementId()),
                result.getHelpType(),
                result.getDate(),
                result.getStartDate(),
                result.getEndDate(),
                result.getSchedules().stream()
                        .map(s -> new ScheduleDTO(
                                s.getDayOfWeek(),
                                s.getStartTime(),
                                s.getEndTime()
                        ))
                        .toList(),
                result.getUnitHoney(),
                result.getTotalHoney(),
                String.valueOf(result.getOtherId()),
                result.getOtherProfileImageUrl(),
                result.getOtherNickname(),
                result.getOtherGender(),
                result.getOtherAgeGroup()
        );
    }

    public record ScheduleDTO(
            String dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    ) {
    }
}
