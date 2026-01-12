package com.lgcns.bebee.member.presentation;

import com.lgcns.bebee.member.application.usecase.AnalyzeDocumentUseCase;
import com.lgcns.bebee.member.application.usecase.ApproveDocumentUseCase;
import com.lgcns.bebee.member.application.usecase.RejectDocumentUseCase;
import com.lgcns.bebee.member.application.usecase.UploadDocumentUseCase;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.service.DocumentManagement;
import com.lgcns.bebee.member.presentation.dto.req.DocumentRejectReqDTO;
import com.lgcns.bebee.member.presentation.dto.res.DocumentUploadResDTO;
import com.lgcns.bebee.member.presentation.dto.res.DocumentVerificationResDTO;
import com.lgcns.bebee.member.presentation.swagger.DocumentSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * 문서 검증 API 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController implements DocumentSwagger {

        private final UploadDocumentUseCase uploadDocumentUseCase;
        private final AnalyzeDocumentUseCase analyzeDocumentUseCase;
        private final ApproveDocumentUseCase approveDocumentUseCase;
        private final RejectDocumentUseCase rejectDocumentUseCase;
        private final DocumentManagement documentManagement;
        private final com.lgcns.bebee.member.application.client.OcrClient ocrClient;

        /**
         * 문서 업로드 및 분석
         * 
         * @param memberId   회원 ID (optional - 없으면 분석만 수행)
         * @param documentId 문서 유형 ID (optional - memberId 없으면 불필요)
         * @param file       업로드 파일 (로컬 환경용, optional)
         * @param fileUrl    S3 파일 URL (S3 환경용, optional)
         * @param role       사용자 역할 (HELPER/DISABLED - memberId 없을 때 필수)
         * @return 업로드 결과 또는 분석 결과
         */
        @PostMapping("/upload")
        public ResponseEntity<?> uploadDocument(
                        @RequestParam(required = false) Long memberId,
                        @RequestParam(required = false) Long documentId,
                        @RequestPart(required = false) MultipartFile file,
                        @RequestParam(required = false) String fileUrl,
                        @RequestParam(required = false) String role) {
                
                // memberId 없으면 → 분석만 수행 (5단계: 회원가입 전 문서 검증)
                if (memberId == null) {
                        // role 검증: 회원가입 전 문서 검증 시 필수
                        if (role == null || role.isBlank()) {
                                throw new IllegalArgumentException("회원가입 전 문서 검증 시 role은 필수입니다.");
                        }
                        if (!java.util.Set.of("HELPER", "DISABLED").contains(role.toUpperCase())) {
                                throw new IllegalArgumentException("role은 HELPER 또는 DISABLED만 허용됩니다.");
                        }
                        // SSRF 방지: S3 URL 패턴만 허용 (일관된 검증)
                        validateS3Url(fileUrl);
                        log.info("문서 분석 요청 (회원가입 전): fileUrl={}, role={}", maskUrl(fileUrl), role);
                        
                        // UseCase 호출 (SSRF 검증은 DocumentVerificationService에서 추가 수행)
                        AnalyzeDocumentUseCase.Param param = new AnalyzeDocumentUseCase.Param(fileUrl, role);
                        AnalyzeDocumentUseCase.Result result = analyzeDocumentUseCase.execute(param);
                        
                        log.info("문서 분석 완료: systemFlag={}", result.systemFlag());
                        return ResponseEntity.ok(result);
                }
                
                // memberId 있으면 → 기존 로직 (분석 + DB 저장)
                log.info("문서 업로드 처리 시작: memberId={}, documentId={}, fileUrl={}, hasFile={}",
                                memberId, documentId, fileUrl, file != null && !file.isEmpty());
                try {
                        UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(memberId, documentId, file,
                                        fileUrl);
                        Long verificationId = uploadDocumentUseCase.execute(param);

                        // 저장된 검증 정보 조회
                        DocumentVerification verification = documentManagement.load(verificationId);

                        DocumentUploadResDTO response = DocumentUploadResDTO.of(
                                        verification.getId(),
                                        verification.getFileUrl(),
                                        verification.getForgeryScore(),
                                        verification.getSystemFlag(),
                                        verification.getStatus().name());

                        log.info("문서 업로드 처리 성공: verificationId={}", verificationId);
                        return ResponseEntity.ok(response);
                } catch (Exception e) {
                        log.error("문서 업로드 중 치명적 오류 발생!", e);
                        throw e;
                }
        }

        /**
         * 문서 검증 정보 조회
         * 
         * @param verificationId 검증 ID
         * @return 검증 정보
         */
        @GetMapping("/{verificationId}")
        public ResponseEntity<DocumentVerificationResDTO> getVerification(
                        @PathVariable Long verificationId) {
                DocumentVerification verification = documentManagement.load(verificationId);
                return ResponseEntity.ok(DocumentVerificationResDTO.from(verification));
        }

        /**
         * PENDING 상태 문서 목록 조회 (관리자용)
         * 
         * @return PENDING 문서 목록
         */
        @GetMapping("/pending")
        public ResponseEntity<List<DocumentVerificationResDTO>> getPendingList() {
                List<DocumentVerification> pendingList = documentManagement.loadPendingList();
                List<DocumentVerificationResDTO> response = pendingList.stream()
                                .map(DocumentVerificationResDTO::from)
                                .toList();
                return ResponseEntity.ok(response);
        }

        /**
         * 문서 승인 (관리자용)
         * 
         * @param verificationId 검증 ID
         * @return 성공 응답
         */
        @PostMapping("/{verificationId}/approve")
        public ResponseEntity<Void> approveDocument(
                        @PathVariable Long verificationId) {
                ApproveDocumentUseCase.Param param = new ApproveDocumentUseCase.Param(verificationId);
                approveDocumentUseCase.execute(param);
                return ResponseEntity.ok().build();
        }

        /**
         * 문서 거절 (관리자용)
         * 
         * @param verificationId 검증 ID
         * @param request        거절 요청 (사유)
         * @return 성공 응답
         */
        @PostMapping("/{verificationId}/reject")
        public ResponseEntity<Void> rejectDocument(
                        @PathVariable Long verificationId,
                        @RequestBody DocumentRejectReqDTO request) {
                RejectDocumentUseCase.Param param = new RejectDocumentUseCase.Param(
                                verificationId,
                                request.getReason());
                rejectDocumentUseCase.execute(param);
                return ResponseEntity.ok().build();
        }

        /**
         * OCR 분석 (단순 텍스트 추출)
         * 
         * @param file    분석할 이미지 파일 (optional)
         * @param fileUrl 분석할 S3 파일 URL (optional)
         * @param role    사용자 역할
         * @return OCR 분석 결과
         */
        @PostMapping("/ocr-extract")
        public ResponseEntity<com.lgcns.bebee.member.application.client.OcrClient.OcrResult> extractOcr(
                        @RequestPart(required = false) MultipartFile file,
                        @RequestParam(required = false) String fileUrl,
                        @RequestParam(required = false) String role) {

                if (fileUrl != null && !fileUrl.isBlank()) {
                        // SSRF 방지: S3 URL 패턴만 허용
                        validateS3Url(fileUrl);
                        log.info("URL 기반 OCR 추출 요청: {}", maskUrl(fileUrl));
                        return ResponseEntity.ok(ocrClient.extract(fileUrl, role));
                }

                if (file == null || file.isEmpty()) {
                        throw new IllegalArgumentException("file 또는 fileUrl 중 하나는 필수입니다.");
                }

                log.info("파일 기반 OCR 추출 요청: {}", file.getOriginalFilename());
                return ResponseEntity.ok(ocrClient.analyze(file, role));
        }

        /**
         * 허용된 호스트 패턴 목록
         */
        private static final Set<String> ALLOWED_HOST_SUFFIXES = Set.of(
                ".s3.amazonaws.com",
                ".s3.ap-northeast-2.amazonaws.com",
                ".cloudfront.net"
        );
        private static final Set<String> ALLOWED_EXACT_HOSTS = Set.of(
                "images.be-bee.link"
        );

        /**
         * S3 URL 유효성 검증 (SSRF 방지) - 호스트 기반 검증
         */
        private void validateS3Url(String url) {
                if (url == null || !url.startsWith("https://")) {
                        log.warn("허용되지 않은 URL: {}", maskUrl(url));
                        throw new IllegalArgumentException("허용되지 않은 URL 형식입니다.");
                }

                try {
                        URL parsedUrl = new URL(url);
                        String host = parsedUrl.getHost();
                        
                        // NPE 방지: 호스트가 없는 URL 처리
                        if (host == null) {
                                log.warn("호스트가 없는 URL: {}", maskUrl(url));
                                throw new IllegalArgumentException("허용되지 않은 URL 형식입니다.");
                        }
                        host = host.toLowerCase();

                        // 정확히 일치하는 호스트 확인
                        if (ALLOWED_EXACT_HOSTS.contains(host)) {
                                return;
                        }

                        // suffix 패턴 확인 (호스트가 해당 suffix로 끝나는지)
                        boolean isAllowed = ALLOWED_HOST_SUFFIXES.stream()
                                .anyMatch(host::endsWith);

                        if (!isAllowed) {
                                log.warn("허용되지 않은 호스트: {}", host);
                                throw new IllegalArgumentException("허용되지 않은 URL 형식입니다.");
                        }
                } catch (MalformedURLException e) {
                        log.warn("잘못된 URL 형식: {}", maskUrl(url));
                        throw new IllegalArgumentException("허용되지 않은 URL 형식입니다.");
                }
        }

        /**
         * URL 마스킹 (민감 정보 로깅 방지)
         */
        private String maskUrl(String url) {
                if (url == null) return null;
                int queryIndex = url.indexOf('?');
                return queryIndex > 0 ? url.substring(0, queryIndex) + "?[MASKED]" : url;
        }

}
