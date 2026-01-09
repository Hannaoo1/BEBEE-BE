SET FOREIGN_KEY_CHECKS = 0;

-- DROP TABLE (존재한다면 먼저 삭제)
DROP TABLE IF EXISTS member_help_category_sync;

-- 회원-도움 유형 매핑 테이블
CREATE TABLE member_help_category_sync (
     member_id BIGINT NOT NULL COMMENT '회원 ID',
     help_category_id BIGINT NOT NULL COMMENT '도움 유형 ID',
     `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
     `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

     CONSTRAINT `PK_MEMBER_HELP_CATEGORY` PRIMARY KEY (`help_category_id`, `member_id`),

     CONSTRAINT `FK_member_help_category_sync_TO_member_sync`
         FOREIGN KEY (member_id)
             REFERENCES member_sync (member_id)
             ON DELETE CASCADE
             ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='회원 도움 유형 매핑 테이블';

SET FOREIGN_KEY_CHECKS = 1;
