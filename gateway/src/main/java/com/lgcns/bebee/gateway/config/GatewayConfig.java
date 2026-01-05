package com.lgcns.bebee.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lgcns.bebee.gateway.properties.GatewayAuthProperties;
import com.lgcns.bebee.gateway.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Gateway 설정 클래스
 */
@Configuration
public class GatewayConfig {

    /**
     * ReactiveRedisTemplate Bean 등록
     * String-String 타입의 Reactive Redis 작업을 위한 템플릿
     */
    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(
        ReactiveRedisConnectionFactory factory
    ) {
        StringRedisSerializer serializer = new StringRedisSerializer();
        RedisSerializationContext<String, String> context = RedisSerializationContext
            .<String, String>newSerializationContext()
            .key(serializer)
            .value(serializer)
            .hashKey(serializer)
            .hashValue(serializer)
            .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    /**
     * ObjectMapper Bean 등록
     * Java 8 Time API 지원을 위한 JavaTimeModule 추가
     * LocalDateTime을 ISO-8601 문자열 형식으로 직렬화
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
