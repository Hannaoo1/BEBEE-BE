package com.lgcns.bebee.payment.presentation;

import com.lgcns.bebee.payment.application.usecase.PreparePaymentUseCase;
import com.lgcns.bebee.payment.presentation.dto.req.PreparePaymentReqDTO;
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

    @Override
    @PostMapping("/prepare")
    public ResponseEntity<PreparePaymentResDTO> preparePayment(
            @RequestBody PreparePaymentReqDTO reqDTO
    ) {
        PreparePaymentUseCase.Param param = reqDTO.toParam();
        PreparePaymentUseCase.Result result = preparePaymentUseCase.execute(param);
        PreparePaymentResDTO resDTO = PreparePaymentResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }
}