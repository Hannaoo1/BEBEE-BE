package com.lgcns.bebee.member.domain.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.lgcns.bebee.common.util.SimilarityUtil;
import com.lgcns.bebee.member.application.client.OcrClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 문서 검증 서비스
 * 업로드된 문서의 위변조 여부를 분석하고 점수를 산출
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentVerificationService {

    private final OcrClient ocrClient;

    /**
     * 업로드된 파일을 분석하고, 위변조 관련 점수와 플래그를 계산
     * 
     * @param file 분석할 파일
     * @param role 사용자 역할 (HELPER 또는 DISABLED)
     * @return 분석 결과
     */
    public AnalysisResult analyze(MultipartFile file, String role, String expectedName, LocalDate expectedBirthDate) {
        int baseScore = calcBaseScore(file);
        int exifScore = calcExifScore(file);
        int ocrScore = calcOcrScore(file, role, expectedName, expectedBirthDate);
        int forgeryScore = calcForgeryScore(baseScore, exifScore, ocrScore);
        String systemFlag = decideSystemFlag(forgeryScore);

        return new AnalysisResult(exifScore, ocrScore, forgeryScore, systemFlag);
    }

    /**
     * OCR 원본 데이터 추출
     */
    public OcrClient.OcrResult extractRawOcr(MultipartFile file, String role) {
        return ocrClient.analyze(file, role);
    }

    /**
     * 파일 크기, 확장자 등 기본 점수 계산
     */
    private int calcBaseScore(MultipartFile file) {
        int score = 100;
        long size = file.getSize();

        // 10KB 미만이면 지나치게 작은 이미지로 보고 감점
        if (size < 10 * 1024) {
            score -= 20;
        }

        String originalName = file.getOriginalFilename();
        if (originalName != null) {
            String lower = originalName.toLowerCase();
            if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                    || lower.endsWith(".png") || lower.endsWith(".pdf"))) {
                // 허용하지 않는 확장자면 강한 감점
                score -= 40;
            }
        }

        return clamp(score);
    }

    /**
     * EXIF 메타데이터 기반 점수 계산
     * - 카메라 제조사, 모델명, 소프트웨어 정보 등을 분석
     * - 메타데이터가 전혀 없으면(스크린샷 등) 감점
     */
    private int calcExifScore(MultipartFile file) {
        int score = 0;
        try (InputStream is = file.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(is);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (directory != null) {
                // 1. 제조사(Make) 또는 모델(Model) 정보가 있는가? (실제 촬영 기기 증거)
                if (directory.containsTag(ExifIFD0Directory.TAG_MAKE) ||
                        directory.containsTag(ExifIFD0Directory.TAG_MODEL)) {
                    score += 60;
                }

                // 2. 소프트웨어(Software) 정보가 'Adobe' 등 편집 툴인가? (위조 의심)
                String software = directory.getString(ExifIFD0Directory.TAG_SOFTWARE);
                if (software != null) {
                    String lower = software.toLowerCase();
                    if (lower.contains("adobe") || lower.contains("photoshop") || lower.contains("edit")) {
                        score -= 30; // 편집 흔적 감점
                    } else {
                        score += 20; // 일반적인 폰 소프트웨어 점수 가점
                    }
                }

                // 3. 메타데이터가 존재한다는 자체로 기본 점수 부여
                score += 20;
            } else {
                // EXIF 정보가 아예 없는 경우 (카카오톡 전송 등으로 손실되었거나 스크린샷일 확률 높음)
                log.warn("파일에 EXIF 메타데이터가 없습니다. (스크린샷 또는 원본 훼손 의심)");
                score = 30;
            }
        } catch (Exception e) {
            log.error("EXIF 분석 중 오류 발생: {}", e.getMessage());
            // 분석 실패 시 보수적으로 접근
            return 50;
        }

        log.info("EXIF 분석 완료 - 산출 점수: {}", score);
        return clamp(score);
    }

    /**
     * OCR 텍스트 인식 기반 점수 계산
     * - 외부 OCR 클라이언트의 신뢰도(confidence)를 0~100 점수로 변환
     * - 키워드가 존재하지 않으면 소폭 감점
     */
    private int calcOcrScore(MultipartFile file, String role, String expectedName, LocalDate expectedBirthDate) {
        if (ocrClient == null) {
            return 75;
        }

        OcrClient.OcrResult result = ocrClient.analyze(file, role);
        if (result == null || result.confidence() == null) {
            return 50;
        }

        int score = (int) Math.round(result.confidence() * 100);

        // 1. 키워드 존재 여부 확인
        if (result.keywords() == null || result.keywords().isEmpty()) {
            score -= 10;
        }

        // 2. 이름 일치 여부 확인 (유사도 알고리즘 도입)
        if (result.fields() != null && expectedName != null) {
            String extractedName = result.fields().get("name");
            if (extractedName != null && !extractedName.trim().isEmpty()) {
                double nameSimilarity = SimilarityUtil.calculateSimilarity(expectedName, extractedName);
                log.info("OCR 이름 유사도 분석: 기대값={}, 추출값={}, 유사도={}", expectedName, extractedName, nameSimilarity);

                if (nameSimilarity < 0.9) { // 90% 미만일 때만 감점 시작
                    if (nameSimilarity < 0.6) {
                        score -= 60; // 60% 미만: 완전 불일치 (도용 의심)
                    } else {
                        score -= 25; // 60%~90%: 미세 불일치 (OCR 인식 오류 가능성, MID 유도)
                    }
                }
            } else {
                score -= 30; // 이름이 추출되지 않음
            }
        }

        // 3. 생년월일 일치 여부 확인 (유사도 알고리즘 도입)
        if (result.fields() != null && expectedBirthDate != null) {
            String extractedBirth = result.fields().get("birth");
            if (extractedBirth != null && !extractedBirth.trim().isEmpty()) {
                String expectedStr = expectedBirthDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                String cleanExtracted = extractedBirth.replaceAll("[^0-9]", "");

                double birthSimilarity = SimilarityUtil.calculateSimilarity(expectedStr, cleanExtracted);
                log.info("OCR 생년월일 유사도 분석: 기대값={}, 추출값={}, 유사도={}", expectedStr, cleanExtracted, birthSimilarity);

                if (birthSimilarity < 0.9) {
                    if (birthSimilarity < 0.6) {
                        score -= 40; // 완전 불일치
                    } else {
                        score -= 15; // 미세 불일치
                    }
                }
            }
        }

        return clamp(score);
    }

    /**
     * 종합 위변조 점수 계산
     * 가중치: base 30% + exif 30% + ocr 40%
     */
    private int calcForgeryScore(int base, int exif, int ocr) {
        double result = base * 0.3 + exif * 0.3 + ocr * 0.4;
        return clamp((int) Math.round(result));
    }

    /**
     * 시스템 플래그 결정
     * 
     * @param score 종합 점수
     * @return LOW(의심 낮음) / MID(중간) / HIGH(의심 높음)
     */
    private String decideSystemFlag(int score) {
        if (score >= 90)
            return "LOW"; // 위변조 의심 낮음 (자동 통과)
        if (score >= 60)
            return "MID"; // 중간 (관리자 검토 필요)
        return "HIGH"; // 의심 높음 (가입 차단)
    }

    /**
     * 점수를 0~100 범위로 제한
     */
    private int clamp(int val) {
        return Math.max(0, Math.min(100, val));
    }

    /**
     * 분석 결과 DTO
     * 
     * @param exifScore    EXIF 점수 (0-100)
     * @param ocrScore     OCR 점수 (0-100)
     * @param forgeryScore 종합 위변조 점수 (0-100)
     * @param systemFlag   시스템 플래그 (LOW/MID/HIGH)
     */
    public record AnalysisResult(
            Integer exifScore,
            Integer ocrScore,
            Integer forgeryScore,
            String systemFlag) {
    }
}
