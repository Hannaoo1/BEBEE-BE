package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.application.usecase.CompleteEngagementUseCase;
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

    // 활동 완료 체크
    @Override
    @PostMapping("/{engagementId}/complete")
    public ResponseEntity<EngagementCompleteResDTO> completeEngagement(
            @CurrentMember Long currentMemberId,
            @PathVariable String engagementId
    ) {

        // DTO → Param 변환
        CompleteEngagementUseCase.Param param = new CompleteEngagementUseCase.Param(
                currentMemberId,
                Long.parseLong(engagementId)
        );

        // UseCase 실행
        CompleteEngagementUseCase.Result result = completeEngagementUseCase.execute(param);

        // Result → ResDTO 변환
        EngagementCompleteResDTO resDTO = EngagementCompleteResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }
}