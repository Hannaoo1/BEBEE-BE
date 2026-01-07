package com.lgcns.bebee.member.presentation.dto.res;

import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 문서 검증 조회 응답 DTO
 */
@Getter
@RequiredArgsConstructor
@Schema(description = "문서 검증 조회 응답 DTO")
public class DocumentVerificationResDTO {

    @Schema(description = "검증 ID", example = "10")
    private final Long verificationId;
    @Schema(description = "문서 이름", example = "장애인증명서")
    private final String docName;
    @Schema(description = "파일 URL", example = "https://example.com/doc.jpg")
    private final String fileUrl;
    @Schema(description = "EXIF 점수 (메타데이터 신뢰도)", example = "95")
    private final Integer exifScore;
    @Schema(description = "OCR 점수 (텍스트 인식 신뢰도)", example = "88")
    private final Integer ocrScore;
    @Schema(description = "위변조 점수 (종합 점수)", example = "10")
    private final Integer forgeryScore;
    @Schema(description = "시스템 판단 플래그 (PASS, REVIEW, REJECT)", example = "PASS")
    private final String systemFlag;
    @Schema(description = "검증 상태 (APPROVED, REJECTED, PENDING)", example = "APPROVED")
    private final String status;
    @Schema(description = "거절/보류 사유", example = "이미지가 흐릿합니다.")
    private final String reason;

    /**
     * 엔티티에서 DTO로 변환
     *
     * @param entity DocumentVerification 엔티티
     * @return DocumentVerificationResDTO
     */
    public static DocumentVerificationResDTO from(DocumentVerification entity) {
        return new DocumentVerificationResDTO(
                entity.getId(),
                entity.getDocument().getDocNameKo(),
                entity.getFileUrl(),
                entity.getExifScore(),
                entity.getOcrScore(),
                entity.getForgeryScore(),
                entity.getSystemFlag(),
                entity.getStatus().name(),
                entity.getReason());
    }
}
