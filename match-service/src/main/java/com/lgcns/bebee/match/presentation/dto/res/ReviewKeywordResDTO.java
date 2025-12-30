package com.lgcns.bebee.match.presentation.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "키워드 정보")
public class ReviewKeywordResDTO {

    @Schema(description = "키워드 ID", example = "1")
    private Integer keywordId;

    @Schema(description = "키워드 설명", example = "시간 약속 잘 지켜요")
    private String description;

    @Schema(description = "긍정 키워드 여부", example = "true")
    private Boolean isPositive;
}
