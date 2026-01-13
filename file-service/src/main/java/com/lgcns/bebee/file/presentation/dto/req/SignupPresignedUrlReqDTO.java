package com.lgcns.bebee.file.presentation.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 전용 Presigned URL 생성 요청 DTO")
public record SignupPresignedUrlReqDTO(
        @Schema(description = "이메일 (회원가입 중인 유저의 식별자)", example = "test@example.com", requiredMode = Schema.RequiredMode.REQUIRED) String email,

        @Schema(description = "원본 파일명 (확장자 포함)", example = "my-id-card.jpg", requiredMode = Schema.RequiredMode.REQUIRED) String originFileName,

        @Schema(description = "파일 MIME 타입", example = "image/jpeg", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {
                "image/jpeg", "image/png", "image/gif", "image/webp" }) String contentType) {
}
