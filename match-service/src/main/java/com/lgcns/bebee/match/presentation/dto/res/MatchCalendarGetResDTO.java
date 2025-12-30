package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetMatchCalendarUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchCalendarGetResDTO {
    private List<LocalDate> activeDates;

    public static MatchCalendarGetResDTO from(GetMatchCalendarUseCase.Result result) {
        return new MatchCalendarGetResDTO(result.getActiveDates());
    }
}
