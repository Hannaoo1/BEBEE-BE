package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.application.usecase.GetMatchCalendarUseCase;
import com.lgcns.bebee.match.application.usecase.GetMatchesByDateUseCase;
import com.lgcns.bebee.match.presentation.dto.res.MatchCalendarGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.MatchesByDateResDTO;
import com.lgcns.bebee.match.presentation.swagger.MatchSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/engagements")
@RequiredArgsConstructor
public class MatchController implements MatchSwagger {
    private final GetMatchesByDateUseCase getMatchesByDateUseCase;
    private final GetMatchCalendarUseCase getMatchCalendarUseCase;

    @GetMapping
    public ResponseEntity<MatchesByDateResDTO> getMatchesByDate(
            @CurrentMember Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String type
    ) {
        GetMatchesByDateUseCase.Param param = new GetMatchesByDateUseCase.Param(memberId, date, type);
        GetMatchesByDateUseCase.Result result = getMatchesByDateUseCase.execute(param);

        return ResponseEntity.ok(MatchesByDateResDTO.from(result));
    }

    @GetMapping("/calendar")
    public ResponseEntity<MatchCalendarGetResDTO> getActiveDayByMonth(
            @CurrentMember Long memberId,
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        GetMatchCalendarUseCase.Param param = new GetMatchCalendarUseCase.Param(
                memberId,
                year,
                month
        );
        GetMatchCalendarUseCase.Result result = getMatchCalendarUseCase.execute(param);

        MatchCalendarGetResDTO response = MatchCalendarGetResDTO.from(result);

        return ResponseEntity.ok(response);
    }
}
