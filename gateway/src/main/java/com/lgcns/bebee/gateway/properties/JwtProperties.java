package com.lgcns.bebee.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String issuer,
        String secret,
        Long accessTokenExpiresTimeIn,
        Long refreshTokenExpiresTimeIn
) {
    public JwtProperties{
        if(issuer == null){
            issuer = "bebee";
        }
    }
}
