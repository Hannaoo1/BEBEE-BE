package com.lgcns.bebee.match.presentation.dto;

import com.lgcns.bebee.match.domain.entity.PostSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostScheduleDTO {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public static PostScheduleDTO from(PostSchedule schedule) {
        return new PostScheduleDTO(
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }
}
