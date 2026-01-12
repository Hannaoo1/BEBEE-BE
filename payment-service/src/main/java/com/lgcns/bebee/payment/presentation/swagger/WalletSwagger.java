package com.lgcns.bebee.payment.presentation.swagger;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.payment.presentation.dto.req.HoneyUseReqDTO;
import com.lgcns.bebee.payment.presentation.dto.res.HoneyBalanceGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Wallet", description = "꿀 관련 API")
public interface WalletSwagger {

    @Operation(
            summary = "내 현재 꿀 개수 조회",
            description = "로그인한 사용자의 현재 잔고를 꿀 단위로 계산하여 개수를 반환합니다. (JWT 토큰 기반 인증)"

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

    @Operation(
            summary = "꿀 사용",
            deprecated = true,
            description = """
                    [DEPRECATED] 이 API는 더 이상 사용되지 않습니다. (서버에서 이벤트로 처리)

                    매칭 성사 시 장애인이 해당 도움 활동에 대한 전체 꿀을 선결제합니다. (안심결제)
                    - 매칭 확인서 수락 api 요청 후, 성공 시 해당 api를 연달아 요청하여 해당 매칭에 참여하는 장애인의 꿀을 사용합니다.
                    - 현재 보유 중인 꿀이 충분하면 잔고에서 즉시 차감 후 꿀 보관소로 이동, 그렇지 않은 경우 예외 처리
                    - 나눔인 경우, 해당 API 사용할 필요 X
                    """,
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HoneyUseReqDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "꿀 사용 요청 예시",
                                            description = "차감할 꿀의 개수",
                                            value = """
                                                {
                                                  "matchId": "20001"
                                                  "useHoney": 2000
                                                }
                                                """
                                    )
                            }
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음"
            )
    })
    ResponseEntity<Void> deductHoneyByMemberId(
            @Parameter(hidden = true)
            @CurrentMember Long memberId,
            HoneyUseReqDTO request
    );
}
