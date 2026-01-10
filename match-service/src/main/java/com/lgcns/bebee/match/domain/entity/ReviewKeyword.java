package com.lgcns.bebee.match.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@IdClass(ReviewKeyword.ReviewKeywordId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewKeyword {

    @Id
    @Column(name = "review_id")
    private Long reviewId;

    @Id
    @Column(name = "keyword_id")
    private Integer keywordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("reviewId")
    @JoinColumn(name = "review_id")
    private Review review;

    public static ReviewKeyword create(Integer keywordId) {
        ReviewKeyword reviewKeyword = new ReviewKeyword();
        reviewKeyword.keywordId = keywordId;
        return reviewKeyword;
    }

    // Review 설정
    public void setReview(Review review) {
        this.review = review;
        this.reviewId = review.getId();
    }

    @Getter
    @NoArgsConstructor
    public static class ReviewKeywordId implements Serializable {
        private Long reviewId;
        private Integer keywordId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ReviewKeywordId)) return false;
            ReviewKeywordId that = (ReviewKeywordId) o;
            return Objects.equals(reviewId, that.reviewId) &&
                    Objects.equals(keywordId, that.keywordId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reviewId, keywordId);
        }
    }
}