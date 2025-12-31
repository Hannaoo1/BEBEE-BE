package com.lgcns.bebee.match.infrastructure.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lgcns.bebee.match.application.usecase.client.RegionCodeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoRegionCodeApi implements RegionCodeClient {
    private final KakaoProperties properties;

    @Override
    public String resolveLegalDongCode(Double latitude, Double longitude) {
        try {
            log.debug("카카오 지역 코드 조회 요청: latitude={}, longitude={}", latitude, longitude);

            KakaoRegionResponse response = buildRestClient()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(properties.coord2RegionCodePath())
                            .queryParam("x", longitude)
                            .queryParam("y", latitude)
                            .build())
                    .retrieve()
                    .body(KakaoRegionResponse.class);

            if (response == null || response.documents().isEmpty()) {
                log.warn("카카오 지역 코드 조회 결과 없음: latitude={}, longitude={}", latitude, longitude);
                return null;
            }

            // B 타입(법정동) 찾기
            String legalDongCode = response.documents().stream()
                    .filter(doc -> "B".equals(doc.regionType()))
                    .findFirst()
                    .map(KakaoRegionResponse.Document::code)
                    .orElse(null);

            log.debug("카카오 지역 코드 조회 완료: code={}", legalDongCode);
            return legalDongCode;

        } catch (Exception e) {
            log.error("카카오 지역 코드 조회 실패: latitude={}, longitude={}", latitude, longitude, e);
            return null;
        }
    }

    private RestClient buildRestClient() {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("Authorization", "KakaoAK " + properties.key())
                .build();
    }

    record KakaoRegionResponse(
            List<Document> documents,
            Meta meta
    ) {
        record Document(
                @JsonProperty("region_type")
                String regionType,

                @JsonProperty("code")
                String code
        ) {
        }

        record Meta(
                @JsonProperty("total_count")
                Integer totalCount
        ) {
        }
    }
}
