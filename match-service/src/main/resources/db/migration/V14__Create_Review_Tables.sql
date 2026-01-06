SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLES
DROP TABLE IF EXISTS review_Keyword;
DROP TABLE IF EXISTS review;

-- review 테이블
CREATE TABLE review (
    review_id BIGINT PRIMARY KEY COMMENT '리뷰 ID',
    engagement_id BIGINT NOT NULL COMMENT '활동 ID',
    reviewer_id BIGINT NOT NULL COMMENT '작성자 ID (member_sync)',
    reviewee_id BIGINT NOT NULL COMMENT '대상자 ID (member_sync)',
    review_direction ENUM('DISABLED_TO_HELPER', 'HELPER_TO_DISABLED') NOT NULL COMMENT '리뷰 방향',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성 시간',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',

    -- 제약조건: 한 활동당 한 사람당 1개 리뷰만
    CONSTRAINT uk_engagement_reviewer UNIQUE (engagement_id, reviewer_id),

    -- 인덱스: 받은 리뷰 조회 성능 향상
    INDEX idx_reviewee (reviewee_id),

    -- 외래키: Engagement 삭제 시 리뷰도 삭제 (CASCADE)
    CONSTRAINT fk_review_engagement
        FOREIGN KEY (engagement_id) REFERENCES engagement(engagement_id) ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='리뷰';

-- review_kwyword 매핑 테이블
CREATE TABLE review_keyword (
    review_id BIGINT NOT NULL COMMENT '리뷰 ID',
    keyword_id INT NOT NULL COMMENT '키워드 ID (1-24)',

    -- 복합키: (review_id, keyword_id)
    PRIMARY KEY (review_id, keyword_id),

    -- 외래키: Review 삭제 시 키워드도 삭제 (CASCADE)
    CONSTRAINT fk_review_keyword_review
        FOREIGN KEY (review_id) REFERENCES review(review_id) ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='리뷰 키워드 매핑';

SET FOREIGN_KEY_CHECKS = 1