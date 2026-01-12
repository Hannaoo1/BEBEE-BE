package com.lgcns.bebee.member.presentation.swagger;

import com.lgcns.bebee.member.presentation.dto.req.DocumentRejectReqDTO;
import com.lgcns.bebee.member.presentation.dto.res.DocumentVerificationResDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.bebee.member.application.client.OcrClient;
import java.util.List;

/**
 * 문서 검증 API Swagger 인터페이스
 * Controller가 이 인터페이스를 구현하여 API 문서화
 */
public interface DocumentSwagger {

    /**
     * 문서 업로드 및 분석
     * memberId 없으면 분석만 수행 (회원가입 전 문서 검증용)
     * memberId 있으면 분석 + DB 저장
     */
    ResponseEntity<?> uploadDocument(
            Long memberId,
            Long documentId,
            MultipartFile file,
            String fileUrl,
            String role);

    /**
     * 문서 검증 정보 조회
     */
    ResponseEntity<DocumentVerificationResDTO> getVerification(Long verificationId);

    /**
     * PENDING 문서 목록 조회 (관리자용)
     */
    ResponseEntity<List<DocumentVerificationResDTO>> getPendingList();

    /**
     * 문서 승인 (관리자용)
     */
    ResponseEntity<Void> approveDocument(Long verificationId);

    /**
     * OCR 분석 (단순 텍스트 추출)
     */
    ResponseEntity<OcrClient.OcrResult> extractOcr(MultipartFile file, String fileUrl, String role);

    /**
     * 문서 거절 (관리자용)
     */
    ResponseEntity<Void> rejectDocument(Long verificationId, DocumentRejectReqDTO request);
}
