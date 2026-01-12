package com.lgcns.bebee.common.data.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ScheduleDTO(
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {
}
