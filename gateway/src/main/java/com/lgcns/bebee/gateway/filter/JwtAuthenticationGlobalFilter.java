package com.lgcns.bebee.gateway.filter;

import com.lgcns.bebee.gateway.exception.AuthenticationErrors;
import com.lgcns.bebee.gateway.exception.AuthenticationException;
import com.lgcns.bebee.gateway.properties.GatewayAuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Gateway 전역에서 JWT 인증을 수행하는 GlobalFilter
 *
 * 동작 순서:
 * 1. 인증 제외 경로 확인
 * 2. Authorization 헤더에서 Bearer 토큰 추출
 * 3. JWT 서명/만료 검증
 * 4. Redis 블랙리스트 확인
 * 5. memberId를 X-Member-Id 헤더에 추가
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String MEMBER_ID_HEADER = "X-Member-Id";

    private final ReactiveJwtTokenValidator jwtTokenValidator;
    private final ReactiveRedisBlacklistChecker blacklistChecker;
    private final GatewayAuthProperties authProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (HttpMethod.OPTIONS.equals(exchange.getRequest().getMethod())) {
            return chain.filter(exchange);
        }

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.debug("JWT 인증 필터 진입: {}", path);

        // 1. 인증 제외 경로 확인
        if (isExcludedPath(path)) {
            log.debug("인증 제외 경로입니다: {}", path);
            return chain.filter(exchange);
        }

        // 2. Authorization 헤더에서 토큰 추출
        String token = resolveToken(request);

        // 3. JWT 검증 및 블랙리스트 확인 (Reactive Chain)
        return jwtTokenValidator.parseClaims(token)
            .flatMap(claims -> blacklistChecker.isBlacklisted(token)
                .flatMap(isBlacklisted -> {
                    if (Boolean.TRUE.equals(isBlacklisted)) {
                        log.warn("블랙리스트에 등록된 토큰으로 요청이 거부되었습니다.");
                        return Mono.error(AuthenticationErrors.INVALID_TOKEN.toException());
                    }
                    return Mono.just(claims);
                })
            )
            // 4. memberId를 X-Member-Id 헤더에 추가
            .flatMap(claims -> {
                String memberId = jwtTokenValidator.extractMemberId(claims);
                ServerHttpRequest mutatedRequest = request.mutate()
                    .header(MEMBER_ID_HEADER, memberId)
                    .build();

                log.debug("인증 성공: memberId={}", memberId);
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            })
            // 5. 에러 핸들링은 GatewayExceptionHandler에서 처리
            .onErrorResume(AuthenticationException.class, e -> {
                log.warn("인증 실패: {}", e.getMessage());
                return Mono.error(e);
            });
    }

    /**
     * Authorization 헤더에서 Bearer 토큰을 추출합니다.
     */
    private String resolveToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(AUTHORIZATION_HEADER);

        if (authHeader == null || authHeader.isEmpty()) {
            throw AuthenticationErrors.TOKEN_MISSING.toException();
        }

        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw AuthenticationErrors.INVALID_TOKEN_TYPE.toException();
        }

        return authHeader.substring(BEARER_PREFIX.length());
    }

    /**
     * 인증 제외 경로인지 확인합니다.
     */
    private boolean isExcludedPath(String path) {
        return authProperties.excludePaths().stream()
            .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    public int getOrder() {
        // 가장 먼저 실행되도록 최우선 순위 설정
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
