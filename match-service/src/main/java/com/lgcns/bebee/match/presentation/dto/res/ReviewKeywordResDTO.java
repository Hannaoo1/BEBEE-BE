package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetReviewKeywordsListUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Schema(description = "선택 가능한 키워드 목록 조회")
public class ReviewKeywordResDTO {

    @Schema(description = "키워드 ID", example = "1")
    private Integer keywordId;

    @Schema(description = "키워드 설명", example = "시간 약속 잘 지켜요")
    private String description;

    @Schema(description = "긍정 키워드 여부", example = "true")
    private Boolean isPositive;

    public static List<ReviewKeywordResDTO> fromList(List<GetReviewKeywordsListUseCase.KeywordDTO> keywords) {
        return keywords.stream()
                .map(keyword -> new ReviewKeywordResDTO(
                        keyword.getKeywordId(), // Integer → Long 변환
                        keyword.getDescription(),
                        keyword.getIsPositive()
                ))
                .toList();
    }
}
