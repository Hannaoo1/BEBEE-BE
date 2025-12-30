package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.GetMatchCalendarUseCase;
import com.lgcns.bebee.match.application.usecase.GetMatchesByDateUseCase;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import com.lgcns.bebee.match.presentation.dto.res.MatchCalendarGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.MatchesByDateGetResDTO;
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
    public ResponseEntity<MatchesByDateGetResDTO> getMatchesByDate(
            @RequestParam String memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam EngagementType engagementType
    ) {
        GetMatchesByDateUseCase.Param param = new GetMatchesByDateUseCase.Param(
                Long.parseLong(memberId),
                date,
                engagementType
        );
        GetMatchesByDateUseCase.Result result = getMatchesByDateUseCase.execute(param);

        MatchesByDateGetResDTO response = MatchesByDateGetResDTO.from(result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/calendar")
    public ResponseEntity<MatchCalendarGetResDTO> getActiveDayByMonth(
            @RequestParam String memberId,
            @RequestParam Integer year,
            @RequestParam Integer month
    ) {
        GetMatchCalendarUseCase.Param param = new GetMatchCalendarUseCase.Param(
                Long.parseLong(memberId),
                year,
                month
        );
        GetMatchCalendarUseCase.Result result = getMatchCalendarUseCase.execute(param);

        MatchCalendarGetResDTO response = MatchCalendarGetResDTO.from(result);

        return ResponseEntity.ok(response);
    }
}
