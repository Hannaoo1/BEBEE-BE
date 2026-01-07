package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetReviewKeywordsListUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

// 리뷰 키워드 목록 응답 DTO
@Schema(description = "리뷰 키워드 목록 응답")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewKeywordResDTO {

    @Schema(description = "선택 가능한 키워드 목록")
    private List<KeywordDTO> keywords;

    // Result → DTO 변환
    public static ReviewKeywordResDTO from(GetReviewKeywordsListUseCase.Result result) {
        List<KeywordDTO> keywords = result.getKeywords().stream()
                .map(KeywordDTO::from)
                .collect(Collectors.toList());

        return new ReviewKeywordResDTO(keywords);
    }

    // 키워드 DTO
    @Schema(description = "키워드 정보")
    @Getter
    @AllArgsConstructor
    public static class KeywordDTO {

        @Schema(description = "키워드 ID", example = "1")
        private Integer keywordId;

        @Schema(description = "키워드 내용", example = "시간 약속 잘 지켜요")
        private String description;

        @Schema(description = "긍정 키워드 여부", example = "true")
        private Boolean isPositive;

        public static KeywordDTO from(GetReviewKeywordsListUseCase.KeywordDTO dto) {
            return new KeywordDTO(
                    dto.getKeywordId(),
                    dto.getDescription(),
                    dto.getIsPositive()
            );
        }
    }
}