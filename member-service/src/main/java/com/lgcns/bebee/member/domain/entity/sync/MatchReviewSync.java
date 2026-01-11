package com.lgcns.bebee.member.domain.entity.sync;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchReviewSync extends BaseTimeEntity {

    @Id
    private Long reviewId;

    @Column(nullable = false)
    private Long matchId;

    @Column(nullable = false)
    private Long reviewerId;

    @Column(nullable = false)
    private Long revieweeId;

    @Column(nullable = false, length = 30)
    private String reviewDirection;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchReviewKeywordSync> keywords = new ArrayList<>();

    public static MatchReviewSync create(
            Long reviewId,
            Long matchId,
            Long reviewerId,
            Long revieweeId,
            String reviewDirection
    ) {
        MatchReviewSync review = new MatchReviewSync();
        review.reviewId = reviewId;
        review.matchId = matchId;
        review.reviewerId = reviewerId;
        review.revieweeId = revieweeId;
        review.reviewDirection = reviewDirection;
        return review;
    }
}
