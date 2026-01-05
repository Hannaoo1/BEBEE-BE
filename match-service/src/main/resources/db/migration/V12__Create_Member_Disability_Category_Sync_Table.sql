SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLE (존재한다면 먼저 삭제)
DROP TABLE IF EXISTS member_disability_category_sync;

-- 회원-장애유형 매핑 테이블
CREATE TABLE member_disability_category_sync (
    member_id BIGINT NOT NULL COMMENT '회원 ID',
    disability_category_id BIGINT NOT NULL COMMENT '장애 유형 ID',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    PRIMARY KEY (member_id, disability_category_id),

    CONSTRAINT `FK_member_disability_category_sync_TO_member_sync`
        FOREIGN KEY (member_id)
            REFERENCES member_sync (member_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='회원 장애 유형 매핑 테이블';

SET FOREIGN_KEY_CHECKS = 1;