package com.lgcns.bebee.member.infrastructure.storage;

import com.lgcns.bebee.member.application.client.FileStorageClient;
import com.lgcns.bebee.member.core.exception.DocumentErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

/**
 * S3 파일 저장 구현체
 * 개발/운영 환경용
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "aws.s3.access-key")
public class S3FileStorage implements FileStorageClient {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file, String directory) {
        // TODO: S3 파일 업로드 구현 필요 (필요 시 추가)
        log.warn("S3FileStorage.upload()는 아직 구현되지 않았습니다.");
        return null;
    }

    @Override
    public MultipartFile download(String fileUrl) {
        try {
            log.info("S3에서 파일 다운로드 시작");

            String key = extractKeyFromUrl(fileUrl);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // v2에서 try-with-resources로 ResponseInputStream을 확실히 닫음
            try (ResponseInputStream<GetObjectResponse> s3ObjectStream = s3Client.getObject(getObjectRequest)) {
                String fileName = key.substring(key.lastIndexOf('/') + 1);
                String contentType = s3ObjectStream.response().contentType();
                byte[] content = s3ObjectStream.readAllBytes();

                log.info("S3 파일 다운로드 완료: {}", fileName);
                return new ByteArrayMultipartFile("file", fileName, contentType, content);
            }

        } catch (IOException e) {
            log.error("S3 파일 읽기 실패: {}", fileUrl, e);
            throw DocumentErrors.FILE_UPLOAD_FAILED.toException(); // 대안 에러 필요 시 추가
        } catch (Exception e) {
            log.error("S3 파일 다운로드 중 예외 발생: {}", fileUrl, e);
            throw DocumentErrors.FILE_UPLOAD_FAILED.toException();
        }
    }

    @Override
    public void delete(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            s3Client.deleteObject(builder -> builder.bucket(bucketName).key(key));
            log.info("S3 파일 삭제 완료: {}", key);
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", fileUrl, e);
        }
    }

    /**
     * S3 URL에서 key 추출
     * 예: https://bebee-storage.s3.ap-northeast-2.amazonaws.com/documents/abc.jpg
     * → documents/abc.jpg
     */
    private String extractKeyFromUrl(String fileUrl) {
        if (fileUrl.contains(".com/")) {
            return fileUrl.substring(fileUrl.indexOf(".com/") + 5);
        }
        throw new IllegalArgumentException("Invalid S3 URL: " + fileUrl);
    }
}
