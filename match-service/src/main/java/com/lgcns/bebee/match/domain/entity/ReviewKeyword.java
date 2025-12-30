package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;
import java.io.Serializable;

@Entity
@Getter
@IdClass(ReviewKeyword.ReviewKeywordId.class)
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewKeyword extends BaseTimeEntity {

    @Id
    @Column(name= "review_id")
    private Long reviewId;

    @Id
    @Column(name= "keyword_id")
    private Integer keywordId;

    // 복합키 클래스
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewKeywordId implements Serializable {
        private Long reviewId;
        private Integer keywordId;
    }
}
