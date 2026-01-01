SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLES
DROP TABLE IF EXISTS Review_Keyword;
DROP TABLE IF EXISTS Review;

-- Review 테이블
CREATE TABLE Review (
    review_id BIGINT NOT NULL PRIMARY KEY COMMENT '리뷰 ID',
    engagement_id BIGINT NOT NULL COMMENT '활동 ID',
    reviewer_id BIGINT NOT NULL COMMENT '리뷰 작성자 ID',
    reviewee_id BIGINT NOT NULL COMMENT '리뷰 대상자 ID',
    review_direction VARCHAR(30) NOT NULL COMMENT '리뷰 방향 (DISABLED_TO_HELPER, HELPER_TO_DISABLED)',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '작성일시',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',

    CONSTRAINT unique_review_per_engagement UNIQUE (engagement_id, reviewer_id),
    -- Foreign Keys
    CONSTRAINT fk_review_engagement FOREIGN KEY (engagement_id)
        REFERENCES Engagement(engagement_id) ON DELETE RESTRICT,
    CONSTRAINT fk_review_reviewer FOREIGN KEY (reviewer_id)
        REFERENCES member_sync(member_id) ON DELETE RESTRICT,
    CONSTRAINT fk_review_reviewee FOREIGN KEY (reviewee_id)
        REFERENCES member_sync(member_id) ON DELETE RESTRICT,

    -- 인덱스
    INDEX idx_review_engagement (engagement_id),
    INDEX idx_review_reviewer (reviewer_id),
    INDEX idx_review_reviewee (reviewee_id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='리뷰 테이블 (양방향 키워드 선택형)';

-- Review_Keyword 테이블 (리뷰-키워드 매핑)
CREATE TABLE Review_Keyword (
    review_keyword_id BIGINT NOT NULL PRIMARY KEY,
    review_id BIGINT NOT NULL COMMENT '리뷰 ID',
    keyword_id INT NOT NULL COMMENT '키워드 ID (1~24)',

    UNIQUE KEY unique_review_keyword (review_id, keyword_id),
    CONSTRAINT fk_review_keyword_review FOREIGN KEY (review_id)
            REFERENCES Review(review_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='리뷰-키워드 매핑 테이블';

SET FOREIGN_KEY_CHECKS = 1