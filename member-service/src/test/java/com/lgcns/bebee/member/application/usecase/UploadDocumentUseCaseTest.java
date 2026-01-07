package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.member.application.client.FileStorageClient;
import com.lgcns.bebee.member.domain.entity.Document;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import com.lgcns.bebee.member.domain.repository.DocumentVerificationRepository;
import com.lgcns.bebee.member.domain.service.DocumentManagement;
import com.lgcns.bebee.member.domain.service.DocumentVerificationService;
import com.lgcns.bebee.member.domain.service.MemberManagement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * UploadDocumentUseCase Unit Test
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UploadDocumentUseCase 단위 테스트")
class UploadDocumentUseCaseTest {

        @Mock
        private FileStorageClient fileStorageClient;

        @Mock
        private DocumentVerificationService verificationService;

        @Mock
        private DocumentManagement documentManagement;

        @Mock
        private MemberManagement memberManagement;

        @Mock
        private DocumentVerificationRepository verificationRepository;

        @InjectMocks
        private UploadDocumentUseCase uploadDocumentUseCase;

        private Member testMember;
        private MockMultipartFile testFile;

        @BeforeEach
        void setUp() {
                testMember = mock(Member.class);
                given(testMember.getId()).willReturn(1L);
                given(testMember.getName()).willReturn("임상준");
                given(testMember.getBirthDate()).willReturn(LocalDate.of(1990, 1, 1));
                given(testMember.getRole()).willReturn(Role.HELPER);

                testFile = new MockMultipartFile(
                                "file",
                                "test-document.jpg",
                                "image/jpeg",
                                new byte[50 * 1024]);
        }

        @Nested
        @DisplayName("execute() 메서드")
        class ExecuteTest {

                @Test
                @DisplayName("정상적인 파라미터로 실행하면 검증 ID를 반환한다")
                void execute_withValidParam_returnsVerificationId() {
                        // given
                        Long memberId = 1L;
                        String uploadedFileUrl = "http://storage.test.com/documents/test-document.jpg";

                        Long documentId = 1L;
                        UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(memberId, documentId,
                                        testFile, null);

                        DocumentVerificationService.AnalysisResult analysisResult = new DocumentVerificationService.AnalysisResult(
                                        80, 75, 77, "LOW");

                        given(fileStorageClient.upload(any(MultipartFile.class), eq("documents")))
                                        .willReturn(uploadedFileUrl);
                        given(memberManagement.getExistingMember(memberId))
                                        .willReturn(testMember);
                        given(verificationService.analyze(any(MultipartFile.class), anyString(), anyString(),
                                        any(LocalDate.class)))
                                        .willReturn(analysisResult);
                        given(verificationRepository.save(any(DocumentVerification.class)))
                                        .willAnswer(invocation -> {
                                                DocumentVerification v = invocation.getArgument(0);
                                                ReflectionTestUtils.setField(v, "documentVerificationId", 999L);
                                                return v;
                                        });

                        // when
                        Long result = uploadDocumentUseCase.execute(param);

                        // then
                        assertThat(result).isEqualTo(999L);
                        then(fileStorageClient).should().upload(testFile, "documents");
                        then(verificationService).should().analyze(eq(testFile), eq("HELPER"), eq("임상준"),
                                        eq(LocalDate.of(1990, 1, 1)));
                        then(verificationRepository).should().save(any(DocumentVerification.class));
                }

                @Test
                @DisplayName("파일 업로드 후 분석 결과가 DocumentVerification에 적용된다")
                void execute_appliesAnalysisResultToVerification() {
                        // given
                        Long memberId = 1L;
                        String uploadedFileUrl = "http://storage.test.com/documents/test-document.jpg";

                        Long documentId = 1L;
                        UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(memberId, documentId,
                                        testFile, null);

                        DocumentVerificationService.AnalysisResult analysisResult = new DocumentVerificationService.AnalysisResult(
                                        85, 70, 76, "MID");

                        given(fileStorageClient.upload(any(MultipartFile.class), eq("documents")))
                                        .willReturn(uploadedFileUrl);
                        given(memberManagement.getExistingMember(memberId))
                                        .willReturn(testMember);
                        given(verificationService.analyze(any(MultipartFile.class), anyString(), anyString(),
                                        any(LocalDate.class)))
                                        .willReturn(analysisResult);
                        given(verificationRepository.save(any(DocumentVerification.class)))
                                        .willAnswer(invocation -> {
                                                DocumentVerification v = invocation.getArgument(0);
                                                ReflectionTestUtils.setField(v, "documentVerificationId", 999L);
                                                return v;
                                        });

                        // when
                        uploadDocumentUseCase.execute(param);

                        // then
                        ArgumentCaptor<DocumentVerification> captor = ArgumentCaptor
                                        .forClass(DocumentVerification.class);
                        then(verificationRepository).should().save(captor.capture());

                        DocumentVerification saved = captor.getValue();
                        assertThat(saved.getFileUrl()).isEqualTo(uploadedFileUrl);
                        assertThat(saved.getExifScore()).isEqualTo(85);
                        assertThat(saved.getOcrScore()).isEqualTo(70);
                        assertThat(saved.getForgeryScore()).isEqualTo(76);
                        assertThat(saved.getSystemFlag()).isEqualTo("MID");
                }

                @Test
                @DisplayName("S3 URL이 제공되면 파일을 다운로드하여 분석을 수행한다")
                void execute_withFileUrl_downloadsAndAnalyzes() {
                        // given
                        Long memberId = 1L;
                        Long documentId = 1L;
                        String s3Url = "https://bebee-storage.s3.ap-northeast-2.amazonaws.com/documents/test-license.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256";
                        UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(memberId, documentId, null,
                                        s3Url);

                        DocumentVerificationService.AnalysisResult analysisResult = new DocumentVerificationService.AnalysisResult(
                                        80, 75, 77, "LOW");

                        given(fileStorageClient.download(s3Url)).willReturn(testFile);
                        given(memberManagement.getExistingMember(memberId)).willReturn(testMember);
                        given(verificationService.analyze(any(), anyString(), anyString(), any()))
                                        .willReturn(analysisResult);
                        given(verificationRepository.save(any(DocumentVerification.class)))
                                        .willAnswer(invocation -> {
                                                DocumentVerification v = invocation.getArgument(0);
                                                ReflectionTestUtils.setField(v, "documentVerificationId", 999L);
                                                return v;
                                        });

                        // when
                        Long result = uploadDocumentUseCase.execute(param);

                        // then
                        assertThat(result).isEqualTo(999L);
                        verify(fileStorageClient, times(1)).download(s3Url);
                        verify(verificationService, times(1)).analyze(any(), anyString(), anyString(), any());
                }

                @Test
                @DisplayName("파일과 S3 URL이 모두 없으면 예외가 발생한다")
                void execute_withoutFileAndUrl_throwsException() {
                        // given
                        Long memberId = 1L;
                        Long documentId = 1L;
                        UploadDocumentUseCase.Param param = new UploadDocumentUseCase.Param(memberId, documentId, null,
                                        null);

                        // when & then
                        assertThatThrownBy(() -> uploadDocumentUseCase.execute(param))
                                        .isInstanceOf(com.lgcns.bebee.member.core.exception.MemberBaseException.class);
                }
        }
}
