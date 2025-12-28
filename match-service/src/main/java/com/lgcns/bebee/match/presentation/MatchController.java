package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.GetMatchesByDateUseCase;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
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
}
