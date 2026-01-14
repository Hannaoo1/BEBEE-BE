package com.lgcns.bebee.member.presentation.dto.res;

import com.lgcns.bebee.member.application.usecase.GetProfileInfoUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "프로필 정보 응답 DTO")
public class ProfileInfoResDTO {
    @Schema(description = "닉네임", example = "꿀벌123")
    private String nickname;

    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @Schema(description = "회원 역할 (DISABLED: 장애인, HELPER: 도우미)", example = "HELPER")
    private String role;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String profileImageUrl;

    @Schema(description = "성별 (MALE: 남성, FEMALE: 여성, NONE: 미설정)", example = "MALE")
    private String gender;

    @Schema(description = "연령대", example = "30")
    private Integer ageGroup;

    @Schema(description = "도로명 주소", example = "서울시 강서구 마곡중앙8로")
    private String address;

    @Schema(description = "도움 카테고리 목록", example = "[\"SMARTPHONE\", \"KIOSK\"]")
    private List<String> helpCategories;

    @Schema(description = "자기소개", example = "안녕하세요, 잘 부탁드립니다.")
    private String introduction;

    @Schema(description = "꿀 포인트", example = "0")
    private Long honey;

    @Schema(description = "받은 후기")
    private List<ReviewKeywordDTO> reviews;

    // 도우미 전용 정보
    @Schema(description = "뱃지 정보 (도우미 전용)")
    private List<BadgeStatusDTO> badges;

    @Schema(description = "제출한 이수증 서류 목록 (도우미 전용)")
    private List<DocumentInfo> documents;

    // 장애인 전용 정보
    @Schema(description = "장애 유형 (장애인 전용)", example = "시각장애")
    private String disabilityType;

    @Schema(description = "장애 등급 (장애인 전용)", example = "1")
    private String disabilityGrade;

    @Schema(description = "장애 상세 설명 (장애인 전용)", example = "양 눈 빛 감지는 가능해요")
    private String disabilityDescription;

    public static ProfileInfoResDTO from(GetProfileInfoUseCase.Result result) {
        return new ProfileInfoResDTO(
                result.getNickname(),
                result.getEmail(),
                result.getRole() != null ? result.getRole().name() : null,
                result.getProfileImageUrl(),
                result.getGender() != null ? result.getGender().name() : null,
                result.getAgeGroup(),
                result.getAddress(),
                result.getHelpCategories(),
                result.getIntroduction(),
                result.getHoney(),
                result.getReviews() != null ?
                        result.getReviews().stream()
                                .map(r -> new ReviewKeywordDTO(r.keywordId(), r.description(), r.isPositive(), r.count()))
                                .toList() : null,
                result.getBadges() != null ?
                        result.getBadges().stream()
                                .map(BadgeStatusDTO::from)
                                .toList() : null,
                result.getDocuments() != null ?
                        result.getDocuments().stream()
                                .map(DocumentInfo::from)
                                .toList() : null,
                result.getDisabilityType(),
                result.getDisabilityGrade(),
                result.getDisabilityDescription()
        );
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "리뷰 키워드 개수 정보")
    public static class ReviewKeywordDTO {
        @Schema(description = "키워드 ID", example = "1")
        private Integer keywordId;

        @Schema(description = "키워드 설명", example = "시간 약속 잘 지켜요")
        private String description;

        @Schema(description = "긍정/부정 여부", example = "true")
        private Boolean isPositive;

        @Schema(description = "해당 키워드를 받은 횟수", example = "5")
        private Long count;
    }

    @Getter
    @AllArgsConstructor
    @Schema(description = "제출 서류 정보")
    public static class DocumentInfo {
        @Schema(description = "서류 ID", example = "1")
        private Long id;

        @Schema(description = "서류 코드", example = "DOC001")
        private String docCode;

        @Schema(description = "서류 이름", example = "장애인증명서")
        private String docName;

        public static DocumentInfo from(GetProfileInfoUseCase.DocumentInfo useCaseDocumentInfo) {
            return new DocumentInfo(
                    useCaseDocumentInfo.getId(),
                    useCaseDocumentInfo.getDocCode(),
                    useCaseDocumentInfo.getDocName()
            );
        }
    }
}
