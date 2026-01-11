-- ========================================
-- V23: Badge 더미 데이터
-- ========================================
-- 뱃지 레벨 기준:
-- 0~4회: NULL (뱃지 없음)
-- 5~9회: LEVEL_1 (숙련자)
-- 10회 이상: LEVEL_2 (전문가)
--
-- 기존 도우미 (member_sync):
-- 700: 강지훈, 800: 윤서연, 900: 임동현, 1000: 한미래

INSERT INTO badge (badge_id, helper_id, disability_category_id, completion_count, badge_code, created_at, updated_at)
VALUES
-- 도우미 700 (강지훈): 다양한 레벨 보유 + 경계값 테스트
(1, 700, 1, 12, 'LEVEL_2', NOW(), NOW()),   -- 지체장애 전문가
(2, 700, 2, 7, 'LEVEL_1', NOW(), NOW()),    -- 시각장애 숙련자
(3, 700, 3, 4, NULL, NOW(), NOW()),         -- 청각장애 4회 (뱃지 없음 경계)
(4, 700, 4, 5, 'LEVEL_1', NOW(), NOW()),    -- 발달장애 5회 (숙련자 경계)
(5, 700, 5, 9, 'LEVEL_1', NOW(), NOW()),    -- 내부기관장애 9회 (숙련자 경계)
(6, 700, 6, 10, 'LEVEL_2', NOW(), NOW()),   -- 기타장애 10회 (전문가 경계)

-- 도우미 800 (윤서연): 전문가 다수
(7, 800, 1, 20, 'LEVEL_2', NOW(), NOW()),   -- 지체장애 전문가
(8, 800, 2, 18, 'LEVEL_2', NOW(), NOW()),   -- 시각장애 전문가
(9, 800, 3, 15, 'LEVEL_2', NOW(), NOW()),   -- 청각장애 전문가
(10, 800, 4, 12, 'LEVEL_2', NOW(), NOW()),  -- 발달장애 전문가
(11, 800, 5, 10, 'LEVEL_2', NOW(), NOW()),  -- 내부기관장애 전문가
(12, 800, 6, 8, 'LEVEL_1', NOW(), NOW()),   -- 기타장애 숙련자

-- 도우미 900 (임동현): 숙련자 다수
(13, 900, 1, 9, 'LEVEL_1', NOW(), NOW()),   -- 지체장애 숙련자
(14, 900, 2, 8, 'LEVEL_1', NOW(), NOW()),   -- 시각장애 숙련자
(15, 900, 3, 7, 'LEVEL_1', NOW(), NOW()),   -- 청각장애 숙련자
(16, 900, 4, 6, 'LEVEL_1', NOW(), NOW()),   -- 발달장애 숙련자
(17, 900, 5, 5, 'LEVEL_1', NOW(), NOW()),   -- 내부기관장애 숙련자

-- 도우미 1000 (한미래): 초보 + 한 분야 집중
(18, 1000, 1, 2, NULL, NOW(), NOW()),       -- 지체장애 2회 (초보)
(19, 1000, 2, 1, NULL, NOW(), NOW()),       -- 시각장애 1회 (초보)
(20, 1000, 4, 25, 'LEVEL_2', NOW(), NOW())  -- 발달장애 고급 전문가 (집중)

ON DUPLICATE KEY UPDATE
    completion_count = VALUES(completion_count),
    badge_code = VALUES(badge_code),
    updated_at = NOW();
