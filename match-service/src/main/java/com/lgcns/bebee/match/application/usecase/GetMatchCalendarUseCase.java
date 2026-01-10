package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMatchCalendarUseCase implements UseCase<GetMatchCalendarUseCase.Param, GetMatchCalendarUseCase.Result> {

    private final MatchRepository matchRepository;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {
        LocalDate monthStart = LocalDate.of(param.getYear(), param.getMonth(), 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        List<Match> matches = matchRepository.findByMonthAndMember(
                param.getMemberId(),
                monthStart,
                monthEnd
        );

        Set<LocalDate> activeDatesSet = matches.stream()
                .map(Match::getAgreement)
                .filter(agreement -> param.getType() == null || agreement.getType() == param.getType())
                .flatMap(agreement -> agreement.getActiveDatesInRange(monthStart, monthEnd).stream())
                .collect(Collectors.toSet());

        List<LocalDate> activeDates = new ArrayList<>(activeDatesSet);
        Collections.sort(activeDates);

        return new Result(activeDates);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final Integer year;
        private final Integer month;
        private final EngagementType type;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private List<LocalDate> activeDates;

        public static Result from(
                List<LocalDate> activeDates
        ) {
            return new Result(activeDates);
        }
    }
}
