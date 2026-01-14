package com.lgcns.bebee.member.presentation.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 회원 정보 응답 DTO (마이페이지용)
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "회원 정보 응답 DTO (마이페이지용)")
public class MemberInfoResDTO {
    // 기본 정보
    @Schema(description = "회원 ID", example = "1")
    private String memberId;
    @Schema(description = "이메일", example = "test@example.com")
    private String email;
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "닉네임", example = "꿀벌123")
    private String nickname;
    @Schema(description = "회원 역할 (DISABLED: 장애인, HELPER: 도우미)", example = "HELPER")
    private String role;
    @Schema(description = "휴대전화 번호", example = "010-1234-5678")
    private String phoneNumber;
    @Schema(description = "자기소개", example = "안녕하세요, 잘 부탁드립니다.")
    private String introduction;
    @Schema(description = "위도", example = "37.5012000")
    private Double latitude;
    @Schema(description = "경도", example = "127.0396000")
    private Double longitude;

    // 프로필 정보
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImageUrl;
    @Schema(description = "당도 (신뢰도)", example = "40.5")
    private BigDecimal sweetness;
    @Schema(description = "꿀 포인트", example = "100")
    private Integer honeyPoint;
    @Schema(description = "도로명 주소", example = "서울시 강서구 마곡중앙8로")
    private String addressRoad;
    @Schema(description = "성별 (MALE: 남성, FEMALE: 여성)", example = "MALE")
    private String gender;
    @Schema(description = "생년월일", example = "1990-01-01")
    private LocalDate birthDate;
    @Schema(description = "연령대", example = "30")
    private Integer ageGroup;

    // 도우미 전용 정보
    @Schema(description = "도움 가능 분야 목록 (도우미 전용)", example = "[\"SMARTPHONE\", \"KIOSK\"]")
    private List<String> helpTypes;
    @Schema(description = "제출한 서류 목록 (도우미 전용)")
    private List<DocumentVerificationResDTO> documents;

    // 장애인 전용 정보
    @Schema(description = "장애명 (장애인 전용)", example = "시각장애")
    private String disabilityType;
    @Schema(description = "장애 상세 설명 (장애인 전용)", example = "시각 장애 1급입니다. 도움이 필요합니다.")
    private String disabilityDescription;
}
