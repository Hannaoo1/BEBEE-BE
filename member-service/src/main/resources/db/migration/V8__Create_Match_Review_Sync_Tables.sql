-- Match Service의 Review 엔티티 동기화 테이블
CREATE TABLE match_review_sync (
    review_id BIGINT NOT NULL PRIMARY KEY COMMENT '리뷰 ID',
    match_id BIGINT NOT NULL COMMENT '매칭 ID',
    reviewer_id BIGINT NOT NULL COMMENT '리뷰 작성자 ID',
    reviewee_id BIGINT NOT NULL COMMENT '리뷰 받는 사람 ID',
    review_direction VARCHAR(30) NOT NULL COMMENT '리뷰 방향',
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일시',
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',
    INDEX idx_reviewee_id (reviewee_id) COMMENT '리뷰 받는 사람 기준 조회용 인덱스'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Match Service Review 동기화 테이블';

-- Match Service의 ReviewKeyword 엔티티 동기화 테이블
CREATE TABLE match_review_keyword_sync (
    review_id BIGINT NOT NULL COMMENT '리뷰 ID',
    keyword_id INT NOT NULL COMMENT '키워드 ID',
    PRIMARY KEY (review_id, keyword_id),
    CONSTRAINT fk_match_review_keyword_sync_review
        FOREIGN KEY (review_id) REFERENCES match_review_sync(review_id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Match Service ReviewKeyword 동기화 테이블';

-- Payment Service의 HoneyWallet 엔티티 동기화 테이블
CREATE TABLE member_honey_wallet_sync (
    honey_wallet_id BIGINT NOT NULL PRIMARY KEY COMMENT '허니 지갑 ID',
    member_id BIGINT NOT NULL UNIQUE COMMENT '회원 ID',
    balance BIGINT NOT NULL DEFAULT 0 COMMENT '잔액 (원 단위)',
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일시',
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',
    INDEX idx_member_id (member_id) COMMENT '회원 기준 조회용 인덱스'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Payment Service HoneyWallet 동기화 테이블';
