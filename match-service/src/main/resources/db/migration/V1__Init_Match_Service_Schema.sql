-- ========================================
-- Match Service Database Schema
-- ========================================
-- 통합 DDL 파일: 모든 테이블 스키마 정의
-- ========================================

SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- 기존 테이블 삭제 (존재한다면)
-- ========================================
DROP TABLE IF EXISTS `application`;
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `engagement`;
DROP TABLE IF EXISTS `match`;
DROP TABLE IF EXISTS `agreement_help_category`;
DROP TABLE IF EXISTS `agreement_schedule`;
DROP TABLE IF EXISTS `agreement_period`;
DROP TABLE IF EXISTS `agreement`;
DROP TABLE IF EXISTS `post_image`;
DROP TABLE IF EXISTS `post_help_category`;
DROP TABLE IF EXISTS `post_schedule`;
DROP TABLE IF EXISTS `post_period`;
DROP TABLE IF EXISTS `post`;
DROP TABLE IF EXISTS `member_help_category_sync`;
DROP TABLE IF EXISTS `member_disability_category_sync`;
DROP TABLE IF EXISTS `member_sync`;
DROP TABLE IF EXISTS `match_member_sync`;

-- ========================================
-- 1. Member Sync Tables (회원 정보 동기화)
-- ========================================

-- 1-1. MemberSync 테이블
CREATE TABLE `member_sync` (
    `member_id`         BIGINT          NOT NULL PRIMARY KEY COMMENT '회원 ID',
    `nickname`          VARCHAR(30)     NOT NULL COMMENT '닉네임',
    `gender`            ENUM('MALE','FEMALE','NONE') NOT NULL DEFAULT 'NONE',
    `birth_date`        DATE            NOT NULL COMMENT '생년월일',
    `role`              ENUM('ADMIN','DISABLED','HELPER') NOT NULL COMMENT '회원 역할',
    `profile_image_url` VARCHAR(512)    NULL COMMENT '프로필 이미지 URL',
    `address_road`      VARCHAR(255)    NULL COMMENT '도로명 주소',
    `latitude`          DECIMAL(10,7)   NOT NULL COMMENT '위도',
    `longitude`         DECIMAL(10,7)   NOT NULL COMMENT '경도',
    `legal_dong_code`   VARCHAR(10)     NOT NULL COMMENT '법정동 코드',
    `created_at`        DATETIME(6)     NOT NULL,
    `updated_at`        DATETIME(6)     NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='회원 정보 동기화 테이블';

-- 1-2. MemberDisabilityCategorySync 테이블 (회원-장애유형 매핑)
CREATE TABLE `member_disability_category_sync` (
    `member_id`             BIGINT      NOT NULL COMMENT '회원 ID',
    `disability_category_id` BIGINT     NOT NULL COMMENT '장애 유형 ID',
    `created_at`            DATETIME(6) NOT NULL,
    `updated_at`            DATETIME(6) NOT NULL,

    PRIMARY KEY (`member_id`, `disability_category_id`),

    CONSTRAINT `FK_member_disability_category_sync_TO_member_sync`
        FOREIGN KEY (`member_id`)
            REFERENCES `member_sync` (`member_id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='회원 장애 유형 매핑 테이블';

-- 1-3. MemberHelpCategorySync 테이블 (회원-도움유형 매핑)
CREATE TABLE `member_help_category_sync` (
    `member_id`         BIGINT   NOT NULL COMMENT '회원 ID',
    `help_category_id`  BIGINT   NOT NULL COMMENT '도움 유형 ID',
    `created_at`        DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `PK_MEMBER_HELP_CATEGORY` PRIMARY KEY (`help_category_id`, `member_id`),

    CONSTRAINT `FK_member_help_category_sync_TO_member_sync`
        FOREIGN KEY (`member_id`)
            REFERENCES `member_sync` (`member_id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='회원 도움 유형 매핑 테이블';

-- ========================================
-- 2. Post Tables (게시글)
-- ========================================

-- 2-1. Post 테이블
CREATE TABLE `post` (
    `post_id`          BIGINT        NOT NULL PRIMARY KEY,
    `member_id`        BIGINT        NOT NULL,
    `title`            VARCHAR(50)   NOT NULL,
    `type`             ENUM('DAY','TERM') NOT NULL,
    `unit_honey`       INT           NOT NULL,
    `total_honey`      INT           NOT NULL,
    `region`           VARCHAR(30)   NOT NULL,
    `status`           ENUM('NON_MATCHED','PROCEEDING','MATCHED') NOT NULL DEFAULT 'NON_MATCHED',
    `legal_dong_code`  VARCHAR(10)   NOT NULL,
    `latitude`         DECIMAL(10,7) NOT NULL,
    `longitude`        DECIMAL(10,7) NOT NULL,
    `applicant_count`  INT           NULL DEFAULT 0,
    `content`          VARCHAR(1000) NULL,
    `created_at`       TIMESTAMP     NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_post_TO_member_sync`
        FOREIGN KEY (`member_id`)
            REFERENCES `member_sync` (`member_id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2-2. PostPeriod 테이블 (게시글 날짜/기간 정보)
CREATE TABLE `post_period` (
    `post_period_id` BIGINT    NOT NULL PRIMARY KEY,
    `post_id`        BIGINT    NOT NULL,
    `start_date`     DATE      NOT NULL,
    `end_date`       DATE      NOT NULL,
    `created_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_post_period_TO_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `post` (`post_id`)
            ON DELETE CASCADE,
    CONSTRAINT `UQ_post_period_post_id` UNIQUE (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2-3. PostSchedule 테이블 (게시글 요일별 스케줄 정보)
CREATE TABLE `post_schedule` (
    `post_schedule_id` BIGINT    NOT NULL PRIMARY KEY,
    `post_id`          BIGINT    NOT NULL,
    `day_of_week`              ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NULL,
    `start_time`       TIME      NULL,
    `end_time`         TIME      NULL,
    `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_post_schedule_TO_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `post` (`post_id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2-4. PostHelpCategory 테이블 (게시글-도움카테고리 매핑)
CREATE TABLE `post_help_category` (
    `post_id`          BIGINT    NOT NULL,
    `help_category_id` BIGINT    NOT NULL,
    `created_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`post_id`, `help_category_id`),

    CONSTRAINT `FK_post_help_category_TO_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `post` (`post_id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2-5. PostImage 테이블 (게시글 이미지)
CREATE TABLE `post_image` (
    `image_id`   BIGINT       NOT NULL PRIMARY KEY,
    `post_id`    BIGINT       NOT NULL,
    `image_url`  VARCHAR(255) NOT NULL,
    `sequence`   INT          NOT NULL,
    `created_at` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_post_image_TO_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `post` (`post_id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ========================================
-- 3. Application Table (신청)
-- ========================================

CREATE TABLE `application` (
    `application_id` BIGINT    NOT NULL PRIMARY KEY,
    `post_id`        BIGINT    NOT NULL,
    `applicant_id`   BIGINT    NOT NULL,
    `is_volunteer`   BOOLEAN   NOT NULL,
    `created_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_application_TO_post`
        FOREIGN KEY (`post_id`)
            REFERENCES `post` (`post_id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ========================================
-- 4. Agreement Tables (매칭 확인서)
-- ========================================

-- 4-1. Agreement 테이블
CREATE TABLE `agreement` (
    `agreement_id`      BIGINT      NOT NULL PRIMARY KEY,
    `post_id`           BIGINT      NOT NULL,
    `helper_id`         BIGINT      NOT NULL,
    `disabled_id`       BIGINT      NOT NULL,
    `unit_honey`        INT         NOT NULL,
    `total_honey`       INT         NOT NULL,
    `region`            VARCHAR(50) NOT NULL,
    `type`              ENUM('DAY','TERM') NOT NULL,
    `confirmation_date` DATE        NOT NULL,
    `status`            ENUM('BEFORE', 'REFUSED', 'CONFIRMED') NOT NULL DEFAULT 'BEFORE',
    `is_volunteer`      BOOLEAN     NOT NULL,
    `created_at`        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_agreement_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 4-2. AgreementPeriod 테이블 (매칭 확인서 날짜/기간 정보)
CREATE TABLE `agreement_period` (
    `agreement_period_id` BIGINT    NOT NULL PRIMARY KEY,
    `agreement_id`        BIGINT    NOT NULL,
    `start_date`          DATE      NOT NULL,
    `end_date`            DATE      NOT NULL,
    `created_at`          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_agreement_period_TO_agreement`
        FOREIGN KEY (`agreement_id`)
            REFERENCES `agreement` (`agreement_id`)
            ON DELETE CASCADE,
    CONSTRAINT `UQ_agreement_period_agreement_id` UNIQUE (`agreement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 4-3. AgreementSchedule 테이블 (매칭 확인서 요일별 스케줄 정보)
CREATE TABLE `agreement_schedule` (
    `agreement_schedule_id` BIGINT    NOT NULL PRIMARY KEY,
    `agreement_id`          BIGINT    NOT NULL,
    `day_of_week`           ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL,
    `start_time`            TIME      NOT NULL,
    `end_time`              TIME      NOT NULL,
    `created_at`            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`            TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_agreement_schedule_TO_agreement`
        FOREIGN KEY (`agreement_id`)
            REFERENCES `agreement` (`agreement_id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 4-4. AgreementHelpCategory 테이블
CREATE TABLE `agreement_help_category` (
    `agreement_id`     BIGINT      NOT NULL,
    `help_category_id` BIGINT      NOT NULL,
    `category_name`    VARCHAR(10) NOT NULL,
    `created_at`       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`agreement_id`, `help_category_id`),

    CONSTRAINT `FK_agreement_help_category_TO_agreement`
        FOREIGN KEY (`agreement_id`)
            REFERENCES `agreement` (`agreement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ========================================
-- 5. Match Table (매칭)
-- ========================================

CREATE TABLE `match` (
    `match_id`           BIGINT       NOT NULL PRIMARY KEY,
    `helper_id`          BIGINT       NOT NULL,
    `disabled_id`        BIGINT       NOT NULL,
    `title`              VARCHAR(100) NOT NULL,
    `chat_room_id`       BIGINT       NOT NULL,
    `agreement_id`       BIGINT       NOT NULL UNIQUE,
    `image_url`          VARCHAR(255) NOT NULL DEFAULT '',
    `created_at`         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `updated_at`         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_match_agreement` FOREIGN KEY (`agreement_id`) REFERENCES `agreement` (`agreement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ========================================
-- 6. Engagement Table (활동)
-- ========================================

CREATE TABLE `engagement` (
    `engagement_id`     BIGINT   NOT NULL PRIMARY KEY,
    `match_id`          BIGINT   NOT NULL,
    `date`              DATE     NOT NULL,
    `type`              ENUM('DAY', 'TERM') NOT NULL,
    `status`            ENUM('NOT_COMPLETED', 'COMPLETED') NULL,
    `is_disabled_check` BOOLEAN  NULL DEFAULT FALSE,
    `is_helper_check`   BOOLEAN  NULL DEFAULT FALSE,
    `created_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `FK_engagement_TO_match`
        FOREIGN KEY (`match_id`)
            REFERENCES `match` (`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ========================================
-- 7. Review Table (리뷰)
-- ========================================

CREATE TABLE `review` (
    review_id BIGINT PRIMARY KEY COMMENT '리뷰 ID (TSID)',
    `match_id` BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL COMMENT '작성자 ID (member_sync)',
    reviewee_id BIGINT NOT NULL COMMENT '대상자 ID (member_sync)',
    review_direction VARCHAR(50) NOT NULL COMMENT '리뷰 방향',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT `fk_review_match` FOREIGN KEY (`match_id`) REFERENCES `match` (`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


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