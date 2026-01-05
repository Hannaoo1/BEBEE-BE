package com.lgcns.bebee.payment.application.port.out;

import com.lgcns.bebee.payment.application.port.out.dto.TempPaymentInfo;

public interface TempPaymentPort {
    String save(Long memberId, Integer amount);

    TempPaymentInfo get(String orderId);

    void delete(String orderId);
}