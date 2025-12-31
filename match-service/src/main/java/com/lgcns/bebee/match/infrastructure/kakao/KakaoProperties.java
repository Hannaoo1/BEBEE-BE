package com.lgcns.bebee.match.infrastructure.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.api")
public record KakaoProperties(
    String key,
    String baseUrl,
    String coord2RegionCodePath,
    String origin,
    Integer timeout
) {
    public KakaoProperties {
        if (baseUrl == null) {
            baseUrl = "https://dapi.kakao.com";
        }
        if (coord2RegionCodePath == null) {
            coord2RegionCodePath = "/v2/local/geo/coord2regioncode";
        }
        if (timeout == null) {
            timeout = 5000;
        }
    }
}