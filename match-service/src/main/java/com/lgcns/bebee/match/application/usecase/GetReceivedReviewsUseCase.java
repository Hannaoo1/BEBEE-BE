package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.ReviewKeyword;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import com.lgcns.bebee.match.domain.service.ReviewManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.lgcns.bebee.common.application.UseCase;

@Service
@RequiredArgsConstructor
public class GetReceivedReviewsUseCase implements UseCase<GetReceivedReviewsUseCase.Param, GetReceivedReviewsUseCase.Result> {

    private final ReviewManager reviewManager;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {
        // 받은 키워드 조회
        List<Review> reviews = reviewManager.findReceivedReviews(param.getMemberId());

        // 모든 키워드 추출
        List<ReviewKeyword> allKeywords = reviews.stream()
                .flatMap(review -> review.getKeywords().stream())
                .toList();

        // 키워드별 횟수 집계
        Map<Integer, Long> keywordCounts = allKeywords.stream()
                .collect(Collectors.groupingBy(
                        ReviewKeyword::getKeywordId,
                        Collectors.counting()
                ));

        // DTO 변환
        List<ReviewStatDTO> stats = keywordCounts.entrySet().stream()
                .map(entry -> {
                    Keyword keyword = Keyword.fromId(entry.getKey());
                    return new ReviewStatDTO(
                            keyword.getId(),
                            keyword.getDescription(),
                            entry.getValue().intValue(),
                            keyword.isPositive()
                    );
                })
                // 마이 페이지가 아니면 긍정 키워드만
                .filter(stat -> param.isMyPage() || stat.isPositive())
                // 횟수 많은 순 정렬
                .sorted(Comparator.comparing(ReviewStatDTO::getCount).reversed())
                .toList();

        return new Result(stats);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final boolean isMyPage;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private List<ReviewStatDTO> stats;
    }

    @Getter
    @AllArgsConstructor
    public static class ReviewStatDTO {
        private Integer keywordId;
        private String description;
        private Integer count;
        private boolean isPositive;
    }
}
