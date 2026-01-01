package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetReceivedReviewsUseCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "받은 후기 통계 응답")
public class ReviewStatsResDTO {

    @Schema(description = "키워드별 통계 리스트")
    private List<KeywordStat> stats;

    public static ReviewStatsResDTO from(GetReceivedReviewsUseCase.Result result) {
        List<KeywordStat> stats = result.getStats().stream()
                .map(stat -> new KeywordStat(
                        stat.getKeywordId(),
                        stat.getDescription(),
                        stat.getCount(),
                        stat.isPositive()
                ))
                .toList();

        return new ReviewStatsResDTO(stats);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "키워드 통계 정보")
    public static class KeywordStat {

        @Schema(description = "키워드 ID", example = "5")
        private Integer keywordId;

        @Schema(description = "키워드 설명", example = "보조 기기 사용에 능숙해요")
        private String description;

        @Schema(description = "받은 횟수", example = "5")
        private Integer count;

        @Schema(description = "긍정 키워드 여부", example = "true")
        private Boolean isPositive;
    }
}
