package com.lgcns.bebee.payment.presentation.swagger;

import com.lgcns.bebee.payment.presentation.dto.req.PreparePaymentReqDTO;
import com.lgcns.bebee.payment.presentation.dto.res.PreparePaymentResDTO;
import io.swagger.v3.oas.annotations.Operation;
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
            description = "토스페이먼츠 결제를 위한 orderId를 생성하고 임시 저장합니다."
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
}