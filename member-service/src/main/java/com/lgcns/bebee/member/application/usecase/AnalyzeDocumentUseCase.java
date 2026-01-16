package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.domain.service.DocumentVerificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 문서 분석 유스케이스 (회원가입 5단계용)
 * - memberId 없이 문서 분석만 수행
 * - systemFlag 계산 후 반환 (DB 저장 X)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyzeDocumentUseCase implements UseCase<AnalyzeDocumentUseCase.Param, AnalyzeDocumentUseCase.Result> {

    private final DocumentVerificationService documentVerificationService;

    /**
     * 문서 분석 실행
     * 
     * @param param 분석 파라미터 (fileUrl, role)
     * @return 분석 결과 (systemFlag 포함)
     */
    @Override
    public Result execute(Param param) {
        param.validate();

        log.info("문서 분석 시작 (회원가입 전): fileUrl={}, role={}", param.getFileUrl(), param.getRole());

        // Domain Service 호출하여 분석 수행
        DocumentVerificationService.AnalysisResult analysis = documentVerificationService.analyze(param.getFileUrl(),
                param.getRole());

        log.info("문서 분석 완료: systemFlag={}, forgeryScore={}",
                analysis.systemFlag(), analysis.forgeryScore());

        return new Result(
                analysis.exifScore(),
                analysis.ocrScore(),
                analysis.forgeryScore(),
                analysis.systemFlag(),
                analysis.fields());
    }

    /**
     * 분석 파라미터
     */
    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final String fileUrl;
        private final String role;

        @Override
        public boolean validate() {
            if (fileUrl == null || fileUrl.isBlank()) {
                throw new IllegalArgumentException("파일 URL은 필수입니다.");
            }
            if (role == null || role.isBlank()) {
                throw new IllegalArgumentException("사용자 역할(role)은 필수입니다.");
            }
            return true;
        }
    }

    /**
     * 분석 결과
     */
    public record Result(
            Integer exifScore,
            Integer ocrScore,
            Integer forgeryScore,
            String systemFlag,
            java.util.Map<String, String> fields) {
    }
}
