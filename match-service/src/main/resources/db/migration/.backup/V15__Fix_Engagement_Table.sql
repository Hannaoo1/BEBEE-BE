-- ========================================
-- Engagement 테이블 수정
-- ========================================

SET FOREIGN_KEY_CHECKS = 0;

-- 1. 상태(status) 컬럼 추가
ALTER TABLE `engagement`
ADD COLUMN `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING'
AFTER `type`;

-- 2. 활동 날짜 추가
ALTER TABLE `engagement`
ADD COLUMN `activity_date` DATE NOT NULL
AFTER `status`;

-- 3. 장애인 완료 체크
ALTER TABLE `engagement`
MODIFY COLUMN `is_disabled_check` BOOLEAN NOT NULL DEFAULT FALSE;

-- 4. 도우미 완료 체크
ALTER TABLE `engagement`
MODIFY COLUMN `is_helper_check` BOOLEAN NOT NULL DEFAULT FALSE;

-- 5. count 컬럼명 변경 (completed_count)
ALTER TABLE `engagement`
CHANGE COLUMN `count` `completed_count` INT NOT NULL DEFAULT 0;

-- 6. 인덱스 추가
CREATE INDEX idx_engagement_status ON engagement(status);
CREATE INDEX idx_engagement_date ON engagement(activity_date);
CREATE INDEX idx_engagement_checks ON engagement(is_disabled_check, is_helper_check);
CREATE INDEX idx_engagement_status_date ON engagement(status, activity_date);

SET FOREIGN_KEY_CHECKS = 1;