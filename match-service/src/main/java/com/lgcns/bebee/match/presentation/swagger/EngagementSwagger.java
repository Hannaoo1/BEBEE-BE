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
            summary = "활동 완료",
            description = """
            ## 동작 방식
            - engagementId 로 식별합니다.

            ## 응답
            - isLastActivity 가 true 이면 리뷰 보내기 버튼 UI
            - isLastActivity 가 true 이면 활동 완료 끝!
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
                                            value = """
                                            {
                                              "isLastEngagement": true
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Engagement 를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            value = """
                                            {
                                                "code": "ENGAGEMENT_NOT_FOUND",
                                                "message": "활동을 찾을 수 없습니다.",
                                                "timestamp": "시간정보"
                                            }
                                            """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<EngagementCompleteResDTO> completeEngagement(
            @Parameter(hidden = true)
            Long currentMemberId,

            @Parameter(
                    description = "계약 ID",
                    example = "10001",
                    required = true
            ) @PathVariable String agreementId
    );
}