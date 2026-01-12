-- AgreementSync 테이블 생성
-- match-service의 Agreement 정보를 동기화하여 Badge 생성 시 사용

CREATE TABLE `agreement_sync` (
    `agreement_id` BIGINT    NOT NULL PRIMARY KEY COMMENT 'Agreement ID',
    `helper_id`    BIGINT    NOT NULL COMMENT '도우미 ID',
    `disabled_id`  BIGINT    NOT NULL COMMENT '장애인 ID',
    `created_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX `IDX_agreement_sync_helper_id` (`helper_id`),
    INDEX `IDX_agreement_sync_disabled_id` (`disabled_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
