package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.application.client.FileStorageClient;
import com.lgcns.bebee.member.domain.entity.Document;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.repository.DocumentRepository;
import com.lgcns.bebee.member.domain.repository.DocumentVerificationRepository;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import com.lgcns.bebee.member.domain.service.DocumentVerificationService;
import com.lgcns.bebee.member.core.exception.DocumentErrors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

/**
 * 문서 업로드 유스케이스
 * 파일 저장 → 위변조 분석 → 검증 결과 저장
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UploadDocumentUseCase implements UseCase<UploadDocumentUseCase.Param, Long> {

    private final FileStorageClient fileStorageClient;
    private final DocumentVerificationService verificationService;
    private final DocumentVerificationRepository verificationRepository;
    private final MemberRepository memberRepository;
    private final DocumentRepository documentRepository;

    /**
     * 문서 업로드 실행
     * 
     * @param param 업로드 파라미터
     * @return 생성된 검증 ID
     */
    @Override
    @Transactional
    public Long execute(Param param) {
        // 파라미터 검증
        param.validate();

        // 0. 회원 조회 (OCR 분석 및 소유권 확인용)
        Member member = memberRepository.findById(param.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId=" + param.getMemberId()));

        log.info("문서 업로드 처리 중... memberName={}, role={}", member.getName(), member.getRole());

        // 1. 파일 준비 (S3 URL이 있으면 다운로드, 없으면 기존 파일 사용)
        MultipartFile fileToAnalyze;
        String fileUrl;

        if (param.getFileUrl() != null && !param.getFileUrl().isBlank()) {
            // S3 URL이 있는 경우: S3에서 다운로드
            try {
                java.net.URI uri = new java.net.URI(param.getFileUrl());
                String path = uri.getPath();
                String fileName = path.substring(path.lastIndexOf('/') + 1);
                log.info("S3 파일 다운로드 시도: {}", fileName);
            } catch (Exception e) {
                log.info("S3 파일 다운로드 시도 (파일명 추출 실패): {}", param.getFileUrl());
            }

            fileToAnalyze = fileStorageClient.download(param.getFileUrl());
            if (fileToAnalyze == null) {
                throw DocumentErrors.FILE_UPLOAD_FAILED.toException();
            }
            fileUrl = param.getFileUrl();
        } else {
            // 로컬 파일인 경우: 업로드 후 URL 받기
            log.info("로컬 파일 업로드 중...");
            fileUrl = fileStorageClient.upload(param.getFile(), "documents");
            if (fileUrl == null || fileUrl.isBlank()) {
                throw DocumentErrors.FILE_UPLOAD_FAILED.toException();
            }
            fileToAnalyze = param.getFile();
        }

        // 2. 위변조 분석 (Domain Service) - 실제 회원 정보 전달
        DocumentVerificationService.AnalysisResult analysis = verificationService.analyze(
                fileToAnalyze,
                member.getRole().name(),
                member.getName(),
                member.getBirthDate());

        // 3. Document 조회 및 소유권 처리 (Domain Service)
        // 만약 프론트에서 보낸 documentId가 없거나, 소유자가 다르면 현재 회원의 전용 문서를 찾거나 생성함
        Document document = documentRepository.findById(param.getDocumentId()).orElse(null);

        if (document == null || !document.getMember().getId().equals(member.getId())) {
            log.warn("요청된 documentId({})가 없거나 현재 회원({})의 소유가 아닙니다. 새 문서를 자동 생성합니다.",
                    param.getDocumentId(), member.getId());

            String docCode = "DOC_" + System.currentTimeMillis();
            String docNameKo = member.getRole().name().equals("HELPER") ? "활동지원사 교육 이수증" : "장애인 복지카드";

            document = Document.create(
                    member.getRole().name(),
                    docCode,
                    docNameKo,
                    docNameKo + " (자동 생성)",
                    member);
            document = documentRepository.save(document);
        }

        // 4. DocumentVerification 생성 및 분석 결과 적용
        DocumentVerification verification = DocumentVerification.of(fileUrl, document);
        verification.applyAnalysisResult(
                analysis.exifScore(),
                analysis.ocrScore(),
                analysis.forgeryScore(),
                analysis.systemFlag());

        // 5. 저장
        verificationRepository.save(verification);
        log.info("문서 업로드 완료. verificationId={}", verification.getId());

        return verification.getId();
    }

    /**
     * 업로드 파라미터
     */
    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final Long documentId;
        private final MultipartFile file; // 로컬 환경용
        private final String fileUrl; // S3 환경용

        @Override
        public boolean validate() {
            log.info("문서 업로드 처리 시작: memberId={}, documentId={}, fileUrl={}, hasFile={}",
                    memberId, documentId, fileUrl, file != null && !file.isEmpty());

            if (memberId == null) {
                throw new IllegalArgumentException("회원 ID는 필수입니다.");
            }
            if (documentId == null) {
                throw new IllegalArgumentException("문서 ID는 필수입니다.");
            }
            // file 또는 fileUrl 중 하나는 필수
            // 조기 검증 (Early Validation)
            if ((file == null || file.isEmpty()) && (fileUrl == null || fileUrl.isBlank())) {
                log.warn("문서 업로드 실패: 파일과 S3 URL이 모두 누락되었습니다. memberId={}", memberId);
                throw new IllegalArgumentException("파일 또는 파일 URL은 필수입니다.");
            }
            return true;
        }
    }
}
