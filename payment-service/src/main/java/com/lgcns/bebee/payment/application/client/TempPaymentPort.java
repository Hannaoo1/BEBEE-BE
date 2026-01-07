package com.lgcns.bebee.payment.application.client;

import com.lgcns.bebee.payment.application.client.dto.TempPaymentInfo;

public interface TempPaymentPort {
    String save(Long memberId, Long amount);

    TempPaymentInfo get(String orderId);

    void delete(String orderId);
}