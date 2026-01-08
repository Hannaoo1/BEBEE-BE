package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.match.presentation.dto.res.EngagementCompleteResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Engagement", description = "활동 완료 API")
public interface EngagementSwagger {

    @Operation(
            summary = "활동 완료 체크",
            description = """
            활동 완료 체크를 처리합니다.
            
            ## 동작 방식
            - Agreement ID를 기반으로 Engagement를 찾아 완료 처리합니다.
            - Engagement가 없으면 자동으로 생성됩니다.
            - 회원 ID는 토큰에서 자동으로 가져옵니다. (헤더에 저장됨)
            
            ## 케이스별 처리
            
            ### 케이스 1: 장애인 체크
            - 즉시 완료 (COMPLETED)
            - is_disabled_check = true
            
            ### 케이스 2: 도우미 체크
            - 대기 (PENDING)
            - is_helper_check = true
            - 3일 후 자동 완료
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "활동 완료 체크 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "장애인 체크 (즉시 완료)",
                                            value = """
                                            {
                                              "status": "COMPLETED",
                                              "isLastActivity": true
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "도우미 체크 (대기)",
                                            value = """
                                            {
                                              "status": "PENDING",
                                              "isLastActivity": true
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agreement를 찾을 수 없음"
            )
    })
    ResponseEntity<EngagementCompleteResDTO> completeEngagement(
            @Parameter(hidden = true)  //  Swagger에 안 보임!
            Long currentMemberId,       // 파라미터 이름만 (어노테이션 없음!)

            @Parameter(
                    description = "계약 ID",
                    example = "10001",
                    required = true
            ) @PathVariable String agreementId
    );
}