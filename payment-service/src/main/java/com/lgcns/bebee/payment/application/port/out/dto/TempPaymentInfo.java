package com.lgcns.bebee.payment.application.port.out.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TempPaymentInfo {
    private final String orderId;
    private final Long amount;
    private final Long memberId;
}