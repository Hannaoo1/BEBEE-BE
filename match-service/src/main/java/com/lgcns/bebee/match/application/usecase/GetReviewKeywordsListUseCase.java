package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.ReviewManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetReviewKeywordsListUseCase implements UseCase<GetReviewKeywordsListUseCase.Param, GetReviewKeywordsListUseCase.Result> {

    /*
     * 작성 가능한 키워드 목록 조회
     *
     * 양방향 리뷰:
     * - 장애인 → 도우미 평가: 키워드 1-12
     * - 도우미 → 장애인 평가: 키워드 13-24
     */

    private final MemberManager memberManager;
    private final ReviewManager reviewManager;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {

        // 작성자 조회
        MemberSync reviewer = memberManager.findExistingMember(param.getReviewerId());

        // ReviewDirection 결정
        ReviewDirection direction = reviewManager.determineReviewDirection(reviewer);

        // 해당 방향의 키워드 목록 조회
        List<Keyword> keywords = Keyword.getByDirection(direction);

        // DTO 변환
        List<KeywordDTO> keywordDTOs = keywords.stream()
                .map(keyword -> new KeywordDTO(
                        keyword.getId(),
                        keyword.getDescription(),
                        keyword.isPositive()
                ))
                .toList();

        return new Result(keywordDTOs);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long reviewerId;
    }

    @Getter
    @AllArgsConstructor
    public static class KeywordDTO {
        private Integer keywordId;
        private String description;
        private Boolean isPositive;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private List<KeywordDTO> keywords;
    }
}
