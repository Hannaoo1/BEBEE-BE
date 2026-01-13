-- ========================================
-- V3: Badge Sync 테이블 추가
-- ========================================
-- 뱃지 동기화 테이블: member-service의 Badge 정보를 match-service에서 조회하기 위한 복제본
-- ========================================

CREATE TABLE `badge_sync` (
    `badge_sync_id`          BIGINT      NOT NULL PRIMARY KEY COMMENT '뱃지 동기화 ID (TSID)',
    `helper_id`              BIGINT      NOT NULL COMMENT '도우미 ID',
    `disability_category_id` BIGINT      NOT NULL COMMENT '장애 유형 ID',
    `completion_count`       INT         NOT NULL DEFAULT 0 COMMENT '활동 완료 횟수',
    `badge_code`             VARCHAR(20) NULL COMMENT '뱃지 코드 (null, LEVEL_1, LEVEL_2)',

    CONSTRAINT `UQ_badge_sync_helper_category` UNIQUE (`helper_id`, `disability_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='뱃지 동기화 테이블';

-- 조회 성능을 위한 인덱스
CREATE INDEX `IDX_badge_sync_helper_id` ON `badge_sync` (`helper_id`);
