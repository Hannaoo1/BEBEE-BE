package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.CompleteEngagementUseCase;
import com.lgcns.bebee.match.presentation.dto.req.EngagementCompleteReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.EngagementCompleteResDTO;
import com.lgcns.bebee.match.presentation.swagger.EngagementSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/engagements")
@RequiredArgsConstructor
public class EngagementController implements EngagementSwagger {

    private final CompleteEngagementUseCase completeEngagementUseCase;

    @Override
    @PostMapping("/{engagementId}/complete")
    public ResponseEntity<EngagementCompleteResDTO> completeEngagement(
            @PathVariable String engagementId,
            @RequestParam String memberId,
            @RequestBody EngagementCompleteReqDTO reqDTO
    ) {
        // DTO → Param 변환
        CompleteEngagementUseCase.Param param = reqDTO.toParam(engagementId, memberId);

        // UseCase 실행
        CompleteEngagementUseCase.Result result = completeEngagementUseCase.execute(param);

        // Result → ResDTO 변환
        EngagementCompleteResDTO resDTO = EngagementCompleteResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }
}