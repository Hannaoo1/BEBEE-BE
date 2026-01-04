package com.lgcns.bebee.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Gateway에서 발생하는 예외를 처리하는 핸들러
 * WebFlux 환경에서는 ErrorWebExceptionHandler를 사용합니다.
 */
@Slf4j
@Component
@Order(-2) // DefaultErrorWebExceptionHandler보다 먼저 실행
@RequiredArgsConstructor
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Gateway 예외 발생: {}", ex.getMessage(), ex);

        // AuthenticationException 처리
        if (ex instanceof AuthenticationException authEx) {
            return handleAuthenticationException(exchange, authEx);
        }

        // 기타 예외는 기본 핸들러에 위임
        return Mono.error(ex);
    }

    /**
     * AuthenticationException을 401 Unauthorized로 응답합니다.
     */
    private Mono<Void> handleAuthenticationException(
        ServerWebExchange exchange,
        AuthenticationException ex
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getError().getCode(),
            ex.getError().getMessage(),
            LocalDateTime.now()
        );

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("JSON 직렬화 실패", e);
            return Mono.error(e);
        }
    }
}
