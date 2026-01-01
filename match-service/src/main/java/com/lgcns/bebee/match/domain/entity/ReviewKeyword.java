package com.lgcns.bebee.match.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewKeyword {

    @Id@Tsid
    @Column(name = "review_keyword_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
    
    @Column(nullable = false)
    private Integer keywordId;

    // ReviewKeyword 생성
    public static ReviewKeyword create(Integer keywordId) {
        ReviewKeyword reviewKeyword = new ReviewKeyword();
        reviewKeyword.keywordId = keywordId;
        return reviewKeyword;
    }

    // Review 연관관계 설정
    public void assignToReview(Review review) {
        this.review = review;
    }
}
