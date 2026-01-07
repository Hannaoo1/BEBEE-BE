package com.lgcns.bebee.member.infrastructure.storage;

import com.lgcns.bebee.member.application.client.FileStorageClient;
import com.lgcns.bebee.member.core.exception.DocumentErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class S3FileStorage implements FileStorageClient {

    @Override
    public String upload(MultipartFile file, String directory) {
        // TODO: S3 파일 업로드 구현 필요 (필요 시 추가)
        log.warn("S3FileStorage.upload()는 아직 구현되지 않았습니다.");
        return null;
    }

    @Override
    public MultipartFile download(String fileUrl) {
        try {
            log.info("S3 서버에서 파일 다운로드 시작 (HTTP 방식): {}", fileUrl);

            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000); // 10초 내에 연결 안 되면 포기
            connection.setReadTimeout(10000); // 10초 내에 파일 안 읽히면 포기
            String contentType = connection.getContentType();

            try (InputStream is = connection.getInputStream()) {
                byte[] content = is.readAllBytes();

                String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
                if (fileName.contains("?")) {
                    fileName = fileName.substring(0, fileName.indexOf("?"));
                }

                log.info("S3 파일 다운로드 및 메모리 로드 완료: {}", fileName);
                return new ByteArrayMultipartFile("file", fileName, contentType, content);
            }

        } catch (Exception e) {
            log.error("S3 파일 다운로드 실패 (HTTP): {}", fileUrl, e);
            throw DocumentErrors.FILE_UPLOAD_FAILED.toException();
        }
    }

    @Override
    public void delete(String fileUrl) {
        // HTTP 방식으로는 삭제가 불가능하며, 관리 서비스(file-service)를 통해 삭제해야 함
        log.warn("S3FileStorage.delete()는 HTTP 방식에서 지원되지 않습니다. 관리 서비스를 이용하세요.");
    }
}
