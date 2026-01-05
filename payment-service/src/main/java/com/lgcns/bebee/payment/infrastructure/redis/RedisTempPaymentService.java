package com.lgcns.bebee.payment.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.bebee.payment.application.port.out.TempPaymentPort;
import com.lgcns.bebee.payment.application.port.out.dto.TempPaymentInfo;
import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import io.hypersistence.utils.hibernate.id.Tsid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTempPaymentService implements TempPaymentPort {

    private static final String KEY_PREFIX = "payment:";
    private static final long TTL_MINUTES = 10;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * orderId 생성 및 결제 정보 임시 저장
     */
    @Override
    public String save(Long memberId, Integer amount) {
        String orderId = Tsid.FactorySupplier.INSTANCE.get().generate().toString();

        TempPaymentInfo dto = new TempPaymentInfo(orderId, amount, memberId);

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
    @Override
    public TempPaymentInfo get(String orderId) {
        String key = KEY_PREFIX + orderId;
        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            log.error("임시 결제 정보 없음: orderId={}", orderId);
            throw PaymentErrors.PAYMENT_NOT_FOUND.toException();
        }

        try {
            return objectMapper.readValue(value, TempPaymentInfo.class);
        } catch (JsonProcessingException e) {
            log.error("Redis 조회 실패", e);
            throw new RuntimeException("임시 결제 정보 조회 실패", e);
        }
    }

    @Override
    public void delete(String orderId) {
        String key = KEY_PREFIX + orderId;
        redisTemplate.delete(key);
    }
}
