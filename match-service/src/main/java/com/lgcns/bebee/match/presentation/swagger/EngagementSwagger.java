package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.presentation.dto.req.EngagementCompleteReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.EngagementCompleteResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Engagement", description = "활동 관리 API")
public interface EngagementSwagger {

    @Operation(
            summary = "활동 완료 체크",
            description = """
                    도우미 또는 장애인이 활동 완료 버튼을 클릭했을 때 호출합니다.
                    
                    **처리 케이스:**
                    - 케이스 1: 둘 다 완료 체크 → 즉시 완료 (COMPLETED)
                    - 케이스 2: 장애인만 완료 체크 → 즉시 완료 (COMPLETED)
                    - 케이스 3: 도우미만 완료 체크 → 대기 상태 유지 (PENDING)
                    
                    **자동 처리:**
                    - 2일 후: 둘 다 미체크 시 경고 알림 발송
                    - 3일 후: 
                      * 도우미만 체크 → 자동 완료 처리
                      * 둘 다 미체크 → 미완료 처리 및 환급
                    
                    **이벤트 발행:**
                    - 완료 시: 정산, 완료 알림, 리뷰 요청 이벤트 발행
                    - 결제 서비스와 알림 서비스가 이벤트를 수신하여 처리
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "활동 완료 체크 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EngagementCompleteResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필수 필드 누락, 유효하지 않은 값)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "활동을 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<EngagementCompleteResDTO> completeEngagement(
            @Parameter(hidden = true)
            @CurrentMember Long currentMemberId,

            @Parameter(
                    description = "활동 ID",
                    required = true,
                    example = "123"
            )
            @PathVariable String engagementId,

            @RequestBody(
                    description = "활동 완료 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EngagementCompleteReqDTO.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody EngagementCompleteReqDTO reqDTO
    );
}