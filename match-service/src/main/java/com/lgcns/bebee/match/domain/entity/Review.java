package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.Keyword;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewKeyword> keywords = new ArrayList<>();

    public static Review create(
            Long engagementId,
            Long reviewerId,
            Long revieweeId,
            List<Keyword> keywords
    ) {
        Review review = new Review();
        review.engagementId = engagementId;
        review.reviewerId = reviewerId;
        review.revieweeId = revieweeId;
        review.addKeywords(keywords);
        return review;
    }
    
    // 키워드 추가
    private void addKeywords(List<Keyword> keywords) {
        this.keywords = keywords.stream()
                .map(keyword -> ReviewKeyword.builder()
                        .reviewId(this.id)
                        .keywordId(keyword.getId())
                        .build())
                .collect(Collectors.toList());
    }
}
