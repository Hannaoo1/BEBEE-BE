package com.lgcns.bebee.payment.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.payment.application.usecase.ConfirmPaymentUseCase;
import com.lgcns.bebee.payment.application.usecase.PreparePaymentUseCase;
import com.lgcns.bebee.payment.presentation.dto.req.ConfirmPaymentReqDTO;
import com.lgcns.bebee.payment.presentation.dto.req.PreparePaymentReqDTO;
import com.lgcns.bebee.payment.presentation.dto.res.ConfirmPaymentResDTO;
import com.lgcns.bebee.payment.presentation.dto.res.PreparePaymentResDTO;
import com.lgcns.bebee.payment.presentation.swagger.PaymentSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController implements PaymentSwagger {

    private final PreparePaymentUseCase preparePaymentUseCase;
    private final ConfirmPaymentUseCase confirmPaymentUseCase;

    @Override
    @PostMapping("/prepare")
    public ResponseEntity<PreparePaymentResDTO> preparePayment(
            @CurrentMember Long currentMemberId,
            @RequestBody PreparePaymentReqDTO reqDTO
    ) {
        PreparePaymentUseCase.Param param = reqDTO.toParam(currentMemberId);
        PreparePaymentUseCase.Result result = preparePaymentUseCase.execute(param);
        PreparePaymentResDTO resDTO = PreparePaymentResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }

    @Override
    @PostMapping("/confirm")
    public ResponseEntity<ConfirmPaymentResDTO> confirmPayment(
            @CurrentMember Long currentMemberId,
            @RequestBody ConfirmPaymentReqDTO reqDTO
    ) {
        ConfirmPaymentUseCase.Param param = reqDTO.toParam(currentMemberId);
        ConfirmPaymentUseCase.Result result = confirmPaymentUseCase.execute(param);
        ConfirmPaymentResDTO resDTO = ConfirmPaymentResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }
}