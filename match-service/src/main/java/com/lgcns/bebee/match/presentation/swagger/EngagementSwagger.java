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

@Tag(name = "Engagement", description = "활동 완료 API")
public interface EngagementSwagger {

    @Operation(
            summary = "활동 완료 체크",
            description = """
            활동 완료 체크를 처리합니다.
            
            ## 인증
            JWT 토큰에서 현재 회원 정보를 자동으로 추출하여 처리합니다.
            - Authorization: Bearer {token} 헤더 필수
            
            ## 동작 방식
            토큰에서 확인된 회원의 역할(Role)에 따라 자동으로 처리됩니다.
            
            ## 케이스별 처리
            
            ### 케이스 1: 장애인이 체크
            - 즉시 완료 처리 (COMPLETED)
            - is_disabled_check = true
            - 활동 완료!
            
            ### 케이스 2: 도우미가 체크
            - 대기 상태 유지 (PENDING)
            - is_helper_check = true
            - 3일 후 스케줄러에서 처리
            
            ### 케이스 3: 도우미 체크 후 3일 경과 (스케줄러)
            - 자동 완료 처리 (COMPLETED)
            - is_disabled_check = true (자동 설정)
            - 활동 완료!
            
            ### 케이스 4: 둘 다 미체크 후 3일 경과 (스케줄러)
            - 미완료 처리 (INCOMPLETED)
            - 환급 처리
            
            ## 응답
            - status: 활동 상태 (COMPLETED / PENDING)
            - isLastActivity: 마지막 활동 여부 (DAY는 항상 true)
            
            ## 예시
```
            장애인이 체크:
            POST /engagements/1001/complete
            Authorization: Bearer {disabled_token}
            → { "status": "COMPLETED", "isLastActivity": true }
            
            도우미가 체크:
            POST /engagements/1002/complete
            Authorization: Bearer {helper_token}
            → { "status": "PENDING", "isLastActivity": true }
```
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
                    responseCode = "401",
                    description = "인증 실패 (토큰 없음 또는 유효하지 않음)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "활동을 찾을 수 없음"
            )
    })
    ResponseEntity<EngagementCompleteResDTO> completeEngagement(
            Long currentMemberId,  // @CurrentMember로 자동 주입됨 (Swagger에서는 표시 안 됨)
            @Parameter(
                    description = "매칭 확인서 ID",
                    example = "1001",
                    required = true
            ) String agreementId
    );
}