package com.lgcns.bebee.chat.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String issuer,
        String secret
) {
    public JwtProperties {
        if (issuer == null) {
            issuer = "bebee";
        }
    }
}