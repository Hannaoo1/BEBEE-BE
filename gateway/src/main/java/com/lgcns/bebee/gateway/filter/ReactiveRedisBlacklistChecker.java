package com.lgcns.bebee.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Reactive 환경에서 Redis 블랙리스트를 확인하는 컴포넌트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReactiveRedisBlacklistChecker {

    private static final String BLACKLIST_PREFIX = "BLACKLIST:";

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    /**
     * 액세스 토큰이 블랙리스트에 있는지 확인합니다.
     *
     * @param accessToken 확인할 액세스 토큰
     * @return Mono<Boolean> - 블랙리스트에 있으면 true, 없으면 false
     */
    public Mono<Boolean> isBlacklisted(String accessToken) {
        String key = BLACKLIST_PREFIX + accessToken;
        return reactiveRedisTemplate.hasKey(key)
            .doOnNext(isBlacklisted -> {
                if (Boolean.TRUE.equals(isBlacklisted)) {
                    log.warn("블랙리스트에 등록된 토큰입니다: {}", key);
                }
            })
            .defaultIfEmpty(false);
    }
}
