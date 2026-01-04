package com.lgcns.bebee.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Gateway 인증 관련 설정 프로퍼티
 */
@ConfigurationProperties(prefix = "gateway.auth")
public record GatewayAuthProperties(
    List<String> excludePaths
) {
    public GatewayAuthProperties {
        if (excludePaths == null) {
            excludePaths = List.of();
        }
    }
}
