package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.domain.entity.Review;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import com.lgcns.bebee.match.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewManager {
    private final ReviewRepository reviewRepository;

    @Transactional
    public Review createReview(
            Long engagementId,
            Long reviewerId,
            Long revieweeId,
            List<Integer> keywordIds
    ) {

        // 키워드 ID → Keyword ENUM 변환
        List<Keyword> keywords = keywordIds.stream()
                .map(Keyword::fromId)
                .toList();

        // 리뷰 생성
        Review review = Review.create(
                engagementId,
                reviewerId,
                revieweeId,
                keywords
        );

        return reviewRepository.save(review);
    }
}
