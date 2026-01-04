package com.lgcns.bebee.gateway.filter;

import com.lgcns.bebee.gateway.exception.AuthenticationErrors;
import com.lgcns.bebee.gateway.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Reactive 환경에서 JWT 토큰을 검증하는 컴포넌트
 */
@Slf4j
@Component
public class ReactiveJwtTokenValidator {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public ReactiveJwtTokenValidator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(
            jwtProperties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * JWT 토큰을 파싱하고 Claims를 반환합니다.
     *
     * @param token JWT 토큰
     * @return Mono<Claims> - 파싱된 Claims를 담은 Mono
     */
    public Mono<Claims> parseClaims(String token) {
        return Mono.fromCallable(() -> {
            try {
                return Jwts.parserBuilder()
                    .requireIssuer(jwtProperties.issuer())
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            } catch (ExpiredJwtException e) {
                log.info("만료된 토큰입니다.");
                throw AuthenticationErrors.TOKEN_EXPIRED.toException();
            } catch (JwtException e) {
                log.warn("유효하지 않은 JWT 토큰입니다. {}", e.getMessage());
                throw AuthenticationErrors.INVALID_TOKEN.toException();
            }
        });
    }

    /**
     * Claims에서 memberId를 추출합니다.
     *
     * @param claims JWT Claims
     * @return memberId (subject)
     */
    public String extractMemberId(Claims claims) {
        return claims.getSubject();
    }
}
