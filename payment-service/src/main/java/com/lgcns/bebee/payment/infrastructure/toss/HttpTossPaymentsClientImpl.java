package com.lgcns.bebee.payment.infrastructure.toss;

import com.lgcns.bebee.payment.application.client.TossPaymentsClient;
import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import io.netty.handler.timeout.TimeoutException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpTossPaymentsClientImpl implements TossPaymentsClient {

    private final WebClient tossPaymentsWebClient;
    private final TossPaymentsProperties properties;

    @Override
    public TossPaymentResponse confirmPayment(String paymentKey, String orderId, Long amount) {
        log.info("토스 결제 승인 요청: paymentKey={}, orderId={}, amount={}", paymentKey, orderId, amount);

        try {
            // 요청 DTO 생성
            TossConfirmRequest request = new TossConfirmRequest(paymentKey, orderId, amount);

            // 토스 API 호출
            TossApiResponse response = tossPaymentsWebClient.post()
                    .uri("/v1/payments/confirm")
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("토스 API 4xx 오류: {}", errorBody);
                                        return Mono.error(PaymentErrors.INVALID_PAYMENT_AMOUNT.toException());
                                    })
                    )
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                        log.error("토스 API 5xx 오류");
                        return Mono.error(PaymentErrors.TOSS_API_ERROR.toException());
                    })
                    .bodyToMono(TossApiResponse.class)
                    .block();

            if (response == null) {
                throw PaymentErrors.TOSS_API_ERROR.toException();
            }

            log.info("토스 결제 승인 성공: paymentKey={}, status={}", paymentKey, response.getStatus());

            return new TossPaymentResponse(
                    response.getPaymentKey(),
                    response.getOrderId(),
                    response.getStatus(),
                    response.getTotalAmount(),
                    response.getMethod(),
                    response.getRequestedAt(),
                    response.getApprovedAt()
            );

        } catch (WebClientResponseException e) {
            log.error("토스 결제 승인 실패: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw PaymentErrors.TOSS_API_ERROR.toException();
        } catch (Exception e) {
            if (e.getCause() instanceof TimeoutException) {
                log.error("토스 API 타임아웃", e);
                throw PaymentErrors.TOSS_API_TIMEOUT.toException();
            }
            log.error("토스 API 호출 중 예외 발생", e);
            throw PaymentErrors.TOSS_API_ERROR.toException();
        }
    }

    @Getter
    private static class TossApiResponse {
        private String paymentKey;
        private String orderId;
        private String status;
        private Integer totalAmount;
        private String method;
        private String requestedAt;
        private String approvedAt;
    }

    private record TossConfirmRequest(
            String paymentKey,
            String orderId,
            Long amount
    ) {}
}
