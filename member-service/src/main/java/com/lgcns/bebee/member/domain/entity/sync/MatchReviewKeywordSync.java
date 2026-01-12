package com.lgcns.bebee.member.domain.entity.sync;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@IdClass(MatchReviewKeywordSync.MatchReviewKeywordId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchReviewKeywordSync {

    @Id
    @Column(name = "review_id")
    private Long reviewId;

    @Id
    @Column(name = "keyword_id")
    private Integer keywordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("reviewId")
    @JoinColumn(name = "review_id")
    private MatchReviewSync review;

    public static MatchReviewKeywordSync create(Integer keywordId) {
        MatchReviewKeywordSync keyword = new MatchReviewKeywordSync();
        keyword.keywordId = keywordId;
        return keyword;
    }

    public void assignToReview(MatchReviewSync review) {
        this.review = review;
        this.reviewId = review.getReviewId();
    }

    @Getter
    @NoArgsConstructor
    public static class MatchReviewKeywordId implements Serializable {
        private Long reviewId;
        private Integer keywordId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MatchReviewKeywordId)) return false;
            MatchReviewKeywordId that = (MatchReviewKeywordId) o;
            return Objects.equals(reviewId, that.reviewId) &&
                    Objects.equals(keywordId, that.keywordId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reviewId, keywordId);
        }
    }
}
