package com.lgcns.bebee.payment.presentation.swagger;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.payment.presentation.dto.req.ConfirmPaymentReqDTO;
import com.lgcns.bebee.payment.presentation.dto.req.PreparePaymentReqDTO;
import com.lgcns.bebee.payment.presentation.dto.res.ConfirmPaymentResDTO;
import com.lgcns.bebee.payment.presentation.dto.res.PreparePaymentResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Payment", description = "결제 관련 API")
public interface PaymentSwagger {

    @Operation(
            summary = "결제 준비",
            description = "토스페이먼츠 결제를 위한 orderId를 생성하고 결제 정보를 임시 저장합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 준비 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreparePaymentResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필수 필드 누락, 유효하지 않은 값 등)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<PreparePaymentResDTO> preparePayment(
            @Parameter(hidden = true)
            @CurrentMember Long currentMemberId,

            @RequestBody(
                    description = "결제 준비 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreparePaymentReqDTO.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody PreparePaymentReqDTO reqDTO
    );

    @Operation(
            summary = "결제 승인",
            description = "임시 저장 데이터와 비교하여 결제 정보를 검증 후 승인하여 꿀을 충전합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 승인 및 꿀 충전 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConfirmPaymentResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (금액 불일치, 중복 결제 등)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Redis에 결제 정보 없음 (만료 또는 존재하지 않음)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류 또는 토스 API 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<ConfirmPaymentResDTO> confirmPayment(
            @Parameter(hidden = true)
            @CurrentMember Long currentMemberId,

            @RequestBody(
                    description = "결제 승인 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConfirmPaymentReqDTO.class)
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody ConfirmPaymentReqDTO reqDTO
    );
}