package com.lgcns.bebee.member.infrastructure.storage;

import com.lgcns.bebee.member.application.client.FileStorageClient;
import com.lgcns.bebee.member.core.exception.DocumentErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * S3 파일 저장 구현체
 * 개발/운영 환경용
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "aws.s3.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class S3FileStorage implements FileStorageClient {

    @Override
    public String upload(MultipartFile file, String directory) {
        throw new UnsupportedOperationException(
                "S3FileStorage는 HTTP 기반으로 파일 업로드를 지원하지 않습니다. " +
                        "file-service를 통해 Presigned URL을 사용하여 S3에 직접 업로드하세요.");
    }

    @Override
    public MultipartFile download(String fileUrl) {
        try {
            // 1. SSRF 방지를 위한 URL 및 도메인 정밀 검증 (CodeRabbit 피드백 반영)
            if (fileUrl == null || fileUrl.isBlank()) {
                throw new IllegalArgumentException("파일 URL은 필수입니다.");
            }

            URL url = new URL(fileUrl);

            // 프로토콜 검증
            if (!"https".equals(url.getProtocol())) {
                log.warn("보안상 HTTPS가 아닌 프로토콜 차단: {}", fileUrl);
                throw new IllegalArgumentException("HTTPS URL만 허용됩니다.");
            }

            // 호스트 도메인 엄격 검증
            String host = url.getHost();
            if (host == null || !host.endsWith(".amazonaws.com")) {
                log.warn("허용되지 않은 도메인 접근 시도 차단 (SSRF 방지): {}", fileUrl);
                throw new IllegalArgumentException("유효한 S3 URL이 아닙니다.");
            }

            log.info("S3 서버에서 파일 다운로드 시작 (HTTP 방식): {}", fileUrl);

            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000); // 10초 내에 연결 안 되면 포기
            connection.setReadTimeout(30000); // 30초로 상항 (대용량 파일 고려)

            // 2. 파일 크기 검증 (OOM 방지, 20MB 제한) - CodeRabbit 피드백 반영
            final long MAX_FILE_SIZE = 20 * 1024 * 1024;
            long contentLength = connection.getContentLengthLong();

            if (contentLength > MAX_FILE_SIZE) {
                log.error("파일 크기가 제한(20MB)을 초과했습니다: {} bytes", contentLength);
                throw new IllegalArgumentException("파일 크기가 너무 큽니다. (최대 20MB)");
            }

            String contentType = connection.getContentType();

            try (InputStream is = connection.getInputStream()) {
                byte[] content;

                if (contentLength < 0) {
                    // Content-Length가 없는 경우(-1)에도 스트리밍 중 크기 제한 강제 (보안 강화)
                    log.info("Content-Length 미지정으로 스트리밍 크기 제한 모드 진입");
                    java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
                    byte[] chunk = new byte[8192];
                    int bytesRead;
                    long totalRead = 0;

                    while ((bytesRead = is.read(chunk)) != -1) {
                        totalRead += bytesRead;
                        if (totalRead > MAX_FILE_SIZE) {
                            log.error("스트리밍 중 파일 크기 제한(20MB) 초과 감지");
                            throw new IllegalArgumentException("파일 크기가 너무 큽니다. (최대 20MB)");
                        }
                        buffer.write(chunk, 0, bytesRead);
                    }
                    content = buffer.toByteArray();
                } else {
                    content = is.readAllBytes();
                }

                String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
                if (fileName.contains("?")) {
                    fileName = fileName.substring(0, fileName.indexOf("?"));
                }

                log.info("S3 파일 다운로드 및 메모리 로드 완료: {}", fileName);
                return new ByteArrayMultipartFile("file", fileName, contentType, content);
            }

        } catch (IllegalArgumentException e) {
            log.error("잘못된 S3 다운로드 요청: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("S3 파일 다운로드 중 예기치 않은 오류 실패 (HTTP): {}", fileUrl, e);
            throw DocumentErrors.FILE_DOWNLOAD_FAILED.toException();
        }
    }

    @Override
    public void delete(String fileUrl) {
        // HTTP 방식으로는 삭제가 불가능하며, 관리 서비스(file-service)를 통해 삭제해야 함
        log.warn("S3FileStorage.delete()는 HTTP 방식에서 지원되지 않습니다. 관리 서비스를 이용하세요.");
    }
}
