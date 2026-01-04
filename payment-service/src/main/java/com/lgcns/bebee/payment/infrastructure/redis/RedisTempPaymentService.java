package com.lgcns.bebee.payment.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import io.hypersistence.utils.hibernate.id.Tsid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTempPaymentService {

    private static final String KEY_PREFIX = "payment:";
    private static final long TTL_MINUTES = 10;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * orderId 생성 및 결제 정보 임시 저장
     */
    public String save(Long memberId, Integer amount) {
        String orderId = Tsid.FactorySupplier.INSTANCE.get().generate().toString();

        TempPaymentDto dto = new TempPaymentDto(orderId, amount, memberId);

        try {
            String key = KEY_PREFIX + orderId;
            String value = objectMapper.writeValueAsString(dto);

            redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(TTL_MINUTES));
            log.info("임시 결제 정보 저장: orderId={}, amount={}", orderId, amount);

            return orderId;
        } catch (JsonProcessingException e) {
            log.error("Redis 저장 실패", e);
            throw new RuntimeException("임시 결제 정보 저장 실패", e);
        }
    }

    /**
     * orderId로 임시 결제 정보 조회
     */
    public TempPaymentDto get(String orderId) {
        String key = KEY_PREFIX + orderId;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            log.error("임시 결제 정보 없음: orderId={}", orderId);
            throw PaymentErrors.PAYMENT_NOT_FOUND.toException();
        }

        try {
            return objectMapper.readValue(value, TempPaymentDto.class);
        } catch (JsonProcessingException e) {
            log.error("Redis 조회 실패", e);
            throw new RuntimeException("임시 결제 정보 조회 실패", e);
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TempPaymentDto {
        private String orderId;
        private Integer amount;
        private Long memberId;
    }
}
