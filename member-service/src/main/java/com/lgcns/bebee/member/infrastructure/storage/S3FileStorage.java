package com.lgcns.bebee.member.infrastructure.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.lgcns.bebee.member.application.client.FileStorageClient;
import com.lgcns.bebee.member.core.exception.DocumentErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * S3 파일 저장 구현체
 * 개발/운영 환경용
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "aws.s3.access-key")
public class S3FileStorage implements FileStorageClient {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket:bebee-storage}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file, String directory) {
        // TODO: 내일 아침 구현 (팀원 file-service 코드 참고)
        throw new UnsupportedOperationException("S3 upload 구현 예정");
    }

    @Override
    public MultipartFile download(String fileUrl) {
        try {
            log.info("S3에서 파일 다운로드 시작: {}", fileUrl);

            // S3 URL에서 key 추출
            String key = extractKeyFromUrl(fileUrl);

            // S3에서 파일 다운로드
            S3Object s3Object = s3Client.getObject(bucketName, key);
            InputStream inputStream = s3Object.getObjectContent();

            // MultipartFile로 변환
            String fileName = key.substring(key.lastIndexOf('/') + 1);
            String contentType = s3Object.getObjectMetadata().getContentType();

            MultipartFile multipartFile = new MockMultipartFile(
                    "file",
                    fileName,
                    contentType,
                    inputStream);

            log.info("S3 파일 다운로드 완료: {}", fileName);
            return multipartFile;

        } catch (Exception e) {
            log.error("S3 파일 다운로드 실패: {}", fileUrl, e);
            throw DocumentErrors.FILE_UPLOAD_FAILED.toException();
        }
    }

    @Override
    public void delete(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            s3Client.deleteObject(bucketName, key);
            log.info("S3 파일 삭제 완료: {}", key);
        } catch (Exception e) {
            log.warn("S3 파일 삭제 실패 (무시): {}", fileUrl, e);
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
