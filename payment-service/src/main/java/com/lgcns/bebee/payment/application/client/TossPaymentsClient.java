package com.lgcns.bebee.payment.application.client;

public interface TossPaymentsClient {
    TossPaymentResponse confirmPayment(String paymentKey, String orderId, Long amount);

//    TossPaymentResponse getPayment(String paymentKey);
//
//    TossPaymentResponse cancelPayment(String paymentKey, String cancelReason);

    record TossPaymentResponse(
            String paymentKey,
            String orderId,
            String status,
            Integer totalAmount,
            String method,
            String requestedAt,
            String approvedAt
    ) {}
}
