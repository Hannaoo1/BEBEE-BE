package com.lgcns.bebee.member.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import jakarta.annotation.PostConstruct;

/**
 * AWS S3 설정
 * 개발/운영 환경에서만 활성화
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "aws.s3.access-key")
public class AwsS3Config {

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    @PostConstruct
    public void validate() {
        if (region == null || region.isBlank()) {
            throw new IllegalStateException("AWS S3 region이 설정되지 않았습니다.");
        }
        if (accessKey == null || accessKey.isBlank()) {
            throw new IllegalStateException("AWS S3 access-key가 설정되지 않았습니다.");
        }
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("AWS S3 secret-key가 설정되지 않았습니다.");
        }
    }

    @Bean
    public S3Client s3Client() {
        log.info("AWS S3 Client (v2) 초기화 시작: region={}", region);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }
}
