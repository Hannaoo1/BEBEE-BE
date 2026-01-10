SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLES
DROP TABLE IF EXISTS review_keyword;
DROP TABLE IF EXISTS review;

-- review 테이블 (TSID 사용!)
CREATE TABLE review (
    review_id BIGINT PRIMARY KEY COMMENT '리뷰 ID (TSID)',
    match_id BIGINT NOT NULL COMMENT '매칭 ID',
    reviewer_id BIGINT NOT NULL COMMENT '작성자 ID (member_sync)',
    reviewee_id BIGINT NOT NULL COMMENT '대상자 ID (member_sync)',
    review_direction VARCHAR(50) NOT NULL COMMENT '리뷰 방향',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- 인덱스
    INDEX idx_review_match (match_id),
    INDEX idx_review_reviewer (reviewer_id),
    INDEX idx_review_reviewee (reviewee_id)

    CONSTRAINT fk_review_match
        FOREIGN KEY (match_id) REFERENCES match(match_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='리뷰';

-- review_keyword 매핑 테이블
CREATE TABLE review_keyword (
    review_id BIGINT NOT NULL COMMENT '리뷰 ID',
    keyword_id INT NOT NULL COMMENT '키워드 ID (1-24)',

    -- 복합키
    PRIMARY KEY (review_id, keyword_id),

    -- 외래키: Review 삭제 시 키워드도 삭제 (CASCADE)
    CONSTRAINT fk_review_keyword_review
        FOREIGN KEY (review_id) REFERENCES review(review_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='리뷰 키워드 매핑';

SET FOREIGN_KEY_CHECKS = 1;