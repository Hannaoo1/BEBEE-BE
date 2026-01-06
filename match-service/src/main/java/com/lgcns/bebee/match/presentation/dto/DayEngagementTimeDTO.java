package com.lgcns.bebee.match.presentation.dto;

import com.lgcns.bebee.match.domain.entity.AgreementPeriod;
import com.lgcns.bebee.match.domain.entity.AgreementSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DayEngagementTimeDTO {
    private LocalDate date;
    private AgreementScheduleDTO schedule;

    public static DayEngagementTimeDTO from(AgreementPeriod period, AgreementSchedule schedule) {
        return new DayEngagementTimeDTO(
                period.getStartDate(),
                AgreementScheduleDTO.from(schedule)
        );
    }
}
