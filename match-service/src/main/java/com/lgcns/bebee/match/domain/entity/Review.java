package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private Long engagementId;

    @Column(nullable = false)
    private Long reviewerId;

    @Column(nullable = false)
    private Long revieweeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReviewDirection reviewDirection;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewKeyword> keywords = new ArrayList<>();

    // 리뷰 생성
    public static Review create(
            Long engagementId,
            Long reviewerId,
            Long revieweeId,
            ReviewDirection reviewDirection,
            List<Integer> keywordIds
    ) {
        Review review = new Review();
        review.engagementId = engagementId;
        review.reviewerId = reviewerId;
        review.revieweeId = revieweeId;
        review.reviewDirection =reviewDirection;
        
        // ReviewKeyword 엔티티 생성
        List<ReviewKeyword> reviewKeywords = keywordIds.stream()
                .map(ReviewKeyword::create)
                .toList();
        reviewKeywords.forEach(keyword -> keyword.assignToReview(review));
        review.keywords = reviewKeywords;

        return review;
    }
}
