package com.lgcns.bebee.payment.presentation.swagger;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.payment.presentation.dto.res.HoneyBalanceGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Honey", description = "꿀 관련 API")
public interface HoneySwagger {

    @Operation(
            summary = "현재 꿀 개수 조회",
            description = "현재 잔고를 꿀 단위로 계산하여 개수를 반환합니다."

    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HoneyBalanceGetResDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "현재 꿀 개수 조회 응답 예시",
                                            value = """
                                                    {
                                                        "currentHoney": 1200
                                                    }
                                                """
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "지갑 정보를 찾을 수 없음")
    })
    ResponseEntity<HoneyBalanceGetResDTO> getHoneyBalanceByMemberId(
            @Parameter(hidden = true)
            @CurrentMember Long memberId
    );
}
