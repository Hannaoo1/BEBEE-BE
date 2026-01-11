-- Badge 테이블 생성
-- 도우미의 장애 유형별 활동 완료 횟수 및 뱃지 레벨 관리

CREATE TABLE `badge` (
    `badge_id`               BIGINT      NOT NULL PRIMARY KEY,
    `helper_id`              BIGINT      NOT NULL COMMENT '도우미 ID',
    `disability_category_id` BIGINT      NOT NULL COMMENT '장애 유형 ID (1~6)',
    `completion_count`       INT         NOT NULL DEFAULT 0 COMMENT '활동 완료 횟수',
    `badge_code`             VARCHAR(20) NULL COMMENT '뱃지 레벨 (NULL, LEVEL_1, LEVEL_2)',
    `created_at`             TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    `updated_at`             TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- 도우미 + 장애유형 조합은 유일해야 함
    CONSTRAINT `UQ_badge_helper_disability` UNIQUE (`helper_id`, `disability_category_id`),

    -- 도우미 ID에 대한 인덱스 (조회 성능)
    INDEX `IDX_badge_helper_id` (`helper_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
