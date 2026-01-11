package com.lgcns.bebee.chat.core.utils;

import com.lgcns.bebee.chat.core.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtTokenValidator {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtTokenValidator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(
                jwtProperties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .requireIssuer(jwtProperties.issuer())
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰입니다.");
            throw e;
        } catch (JwtException e) {
            log.warn("유효하지 않은 JWT 토큰입니다. {}", e.getMessage());
            throw e;
        }
    }

    public Long extractMemberId(Claims claims) {
        return Long.parseLong(claims.getSubject());
    }
}