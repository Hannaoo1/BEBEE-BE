package com.lgcns.bebee.payment.common.exception;

import com.lgcns.bebee.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class PaymentExceptionHandler {
    private static final Map<PaymentErrors, HttpStatus> STATUS_MAP = Map.of(
            PaymentErrors.PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND
    );

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handleException(PaymentException e){
        log.error("Advice 내 handleException() 호출, {}", e.getMessage(), e);

        PaymentErrors error = e.getError();
        HttpStatus httpStatus = STATUS_MAP.getOrDefault(error, HttpStatus.BAD_REQUEST);

        ErrorResponse response = new ErrorResponse(
                error.name(),
                error.getDesc(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(httpStatus).body(response);
    }
}
