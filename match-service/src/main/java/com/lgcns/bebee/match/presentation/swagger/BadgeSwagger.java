package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.presentation.dto.res.HelperBadgesGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Badge", description = "뱃지 API")
public interface BadgeSwagger {

    @Operation(
            summary = "도우미 뱃지 전체 조회",
            description = """
                    로그인한 도우미의 장애 유형별 뱃지 정보를 조회합니다.

                    **뱃지 레벨 기준:**
                    - 활동 완료 0~4회: 뱃지 없음 (badgeCode = null)
                    - 활동 완료 5~9회: LEVEL_1 (숙련자)
                    - 활동 완료 10회 이상: LEVEL_2 (전문가)

                    **장애 유형 (disabilityCategoryId):**
                    - 1: 지체장애
                    - 2: 시각장애
                    - 3: 청각장애
                    - 4: 발달장애
                    - 5: 내부기관장애
                    - 6: 기타장애

                    **반환 정보:**
                    6개 장애 유형 전체에 대한 뱃지 정보를 반환합니다.
                    활동 기록이 없는 장애 유형도 completionCount=0, badgeCode=null로 포함됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "뱃지 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HelperBadgesGetResDTO.class),
                            examples = @ExampleObject(
                                    name = "뱃지 조회 성공 예시",
                                    description = "도우미의 6개 장애 유형별 뱃지 정보",
                                    value = """
                                    {
                                      "badges": [
                                        {
                                          "disabilityCategoryIds": [1],
                                          "completionCount": 5,
                                          "badgeCode": "LEVEL_1"
                                        },
                                        {
                                          "disabilityCategoryIds": [2],
                                          "completionCount": 10,
                                          "badgeCode": "LEVEL_2"
                                        },
                                        {
                                          "disabilityCategoryIds": [3],
                                          "completionCount": 0,
                                          "badgeCode": null
                                        },
                                        {
                                          "disabilityCategoryIds": [4],
                                          "completionCount": 0,
                                          "badgeCode": null
                                        },
                                        {
                                          "disabilityCategoryIds": [5],
                                          "completionCount": 2,
                                          "badgeCode": null
                                        },
                                        {
                                          "disabilityCategoryIds": [6],
                                          "completionCount": 0,
                                          "badgeCode": null
                                        }
                                      ]
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 - 유효하지 않은 토큰",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음 - 도우미만 조회 가능",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<HelperBadgesGetResDTO> getHelperBadges(
            @Parameter(hidden = true) @CurrentMember Long currentMemberId
    );
}
