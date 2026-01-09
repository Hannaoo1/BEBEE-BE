package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.match.presentation.dto.res.HelperBadgesGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Badge", description = "뱃지 API")
public interface BadgeSwagger {

    @GetMapping
    @Operation(summary = "도우미 뱃지 현황 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "뱃지 조회 성공",
                                    value = """
                                    {
                                      "badges": [
                                        {
                                          "disabilityCategoryIds": [1],
                                          "count": 10,
                                          "badgeCode": "LEVEL_1"
                                        },
                                        {
                                          "disabilityCategoryIds": [2],
                                          "count": 0,
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
                    description = "인증 실패"
            )
    })
    ResponseEntity<HelperBadgesGetResDTO> getHelperBadges(Long currentMemberId);
}