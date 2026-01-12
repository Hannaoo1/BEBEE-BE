package com.lgcns.bebee.member.infrastructure.ocr;

import com.lgcns.bebee.member.application.client.OcrClient;
import com.lgcns.bebee.member.core.exception.DocumentErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

/**
 * HTTP 기반 OCR 클라이언트 구현체
 * Python OCR 서비스와 통신
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpOcrClientImpl implements OcrClient {

    private final WebClient ocrWebClient;

    /**
     * 이미지 파일에서 텍스트를 추출하고 분석
     *
     * @param file 분석할 이미지 파일
     * @param role 사용자 역할
     * @return OCR 분석 결과
     */
    @Override
    public OcrResult analyze(MultipartFile file, String role) {
        try {
            log.debug("OCR 분석 요청 (파일): {}, role: {}", file.getOriginalFilename(), role);

            Resource resource = file.getResource();
            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("file", resource);
            parts.add("role", role);

            OcrResponse response = ocrWebClient.post()
                    .uri("/api/ocr/analyze")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(parts))
                    .retrieve()
                    .bodyToMono(OcrResponse.class)
                    .block();

            return toOcrResult(response);

        } catch (WebClientResponseException e) {
            log.error("OCR 서비스 호출 실패: status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw DocumentErrors.OCR_FAILED.toException();
        } catch (Exception e) {
            log.error("OCR 분석 중 예외 발생", e);
            throw DocumentErrors.OCR_FAILED.toException();
        }
    }

    @Override
    public OcrResult extract(String fileUrl, String role) {
        try {
            log.debug("OCR 추출 요청 (URL): {}, role: {}", fileUrl, role);

            // S3 이미지 URL과 사용자 역할을 JSON 바디로 전송
            Map<String, String> requestBody = Map.of(
                    "fileUrl", fileUrl,
                    "role", role != null ? role : "");

            OcrResponse response = ocrWebClient.post()
                    .uri("/api/ocr/extract") // 파이썬 서비스의 새로운 엔드포인트
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(OcrResponse.class)
                    .block();

            return toOcrResult(response);

        } catch (WebClientResponseException e) {
            log.error("OCR 서비스 호출 실패 (URL): status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw DocumentErrors.OCR_FAILED.toException();
        } catch (Exception e) {
            log.error("OCR 추출 중 예외 발생 (URL)", e);
            throw DocumentErrors.OCR_FAILED.toException();
        }
    }

    private OcrResult toOcrResult(OcrResponse response) {
        if (response == null) {
            throw DocumentErrors.OCR_FAILED.toException();
        }

        log.debug("OCR 처리 완료: confidence={}, keywords={}, names={}, fields={}",
                response.getConfidence(), response.getKeywords(), response.getNames(), response.getFields());

        return new OcrResult(
                response.getExtractedText(),
                response.getConfidence(),
                response.getKeywords(),
                response.getNames(),
                response.getFields());
    }

    /**
     * OCR 서비스 응답 DTO
     */
    @lombok.Getter
    @lombok.Setter
    private static class OcrResponse {
        private String extractedText;
        private Double confidence;
        private Integer ocrScore;
        private List<String> keywords;
        private List<String> names;
        private Map<String, String> fields;
    }
}
