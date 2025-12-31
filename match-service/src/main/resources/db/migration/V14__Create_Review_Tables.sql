SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLES
DROP TABLE IF EXISTS Review_Keyword;
DROP TABLE IF EXISTS Review;

-- Review 테이블
CREATE TABLE Review (
    review_id BIGINT NOT NULL PRIMARY KEY COMMENT '리뷰 ID (Snowflake)',
    engagement_id BIGINT NOT NULL COMMENT '활동 ID',
    reviewer_id BIGINT NOT NULL COMMENT '리뷰 작성자 ID',
    reviewee_id BIGINT NOT NULL COMMENT '리뷰 대상자 ID',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '작성일시'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='리뷰 테이블 (양방향 키워드 선택형)';

-- Review_Keyword 테이블 (리뷰-키워드 매핑)
CREATE TABLE Review_Keyword (
    keyword_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '매핑 ID',
    review_id BIGINT NOT NULL COMMENT '리뷰 ID',
    keyword_id_value INT NOT NULL COMMENT '키워드 ENUM ID (1-24)',

    CONSTRAINT FK_Review_Keyword_TO_Review
        FOREIGN KEY (review_id)
        REFERENCES Review (review_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='리뷰-키워드 매핑 테이블';

-- 인덱스 생성 (조회 성능 최적화)
CREATE INDEX idx_engagement ON Review (engagement_id);
CREATE INDEX idx_reviewer ON Review (reviewer_id);
CREATE INDEX idx_reviewee ON Review (reviewee_id);

-- 중복 리뷰 방지 (같은 활동에 같은 사람이 2번 리뷰 불가)
CREATE UNIQUE INDEX uk_engagement_reviewer ON Review (engagement_id, reviewer_id);

-- Review_Keyword 인덱스
CREATE INDEX idx_review ON Review_Keyword (review_id);
CREATE INDEX idx_keyword_value ON Review_Keyword (keyword_id_value);

SET FOREIGN_KEY_CHECKS = 1