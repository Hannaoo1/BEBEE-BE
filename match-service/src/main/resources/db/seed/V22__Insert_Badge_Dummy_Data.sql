-- ========================================
-- V22: Badge 더미 데이터
-- ========================================
-- 뱃지 레벨 기준:
-- 0~4회: NULL (뱃지 없음)
-- 5~9회: LEVEL_1 (숙련자)
-- 10회 이상: LEVEL_2 (전문가)

INSERT INTO badge (badge_id, helper_id, disability_category_id, completion_count, badge_code, created_at, updated_at)
VALUES
-- 도우미 1100: 다양한 레벨 보유
(1, 1100, 1, 12, 'LEVEL_2', NOW(), NOW()),  -- 지체장애 전문가
(2, 1100, 2, 7, 'LEVEL_1', NOW(), NOW()),   -- 시각장애 숙련자
(3, 1100, 3, 3, NULL, NOW(), NOW()),        -- 청각장애 없음
(4, 1100, 4, 15, 'LEVEL_2', NOW(), NOW()),  -- 발달장애 전문가

-- 도우미 1101: 청각장애 전문가
(5, 1101, 3, 10, 'LEVEL_2', NOW(), NOW()),  -- 청각장애 전문가
(6, 1101, 5, 5, 'LEVEL_1', NOW(), NOW()),   -- 내부기관장애 숙련자

-- 도우미 1102: 초보
(7, 1102, 1, 2, NULL, NOW(), NOW()),        -- 지체장애 2회
(8, 1102, 6, 1, NULL, NOW(), NOW()),        -- 기타장애 1회

-- 도우미 1103: 전문가 다수
(9, 1103, 1, 20, 'LEVEL_2', NOW(), NOW()),
(10, 1103, 2, 18, 'LEVEL_2', NOW(), NOW()),
(11, 1103, 3, 15, 'LEVEL_2', NOW(), NOW()),
(12, 1103, 4, 12, 'LEVEL_2', NOW(), NOW()),
(13, 1103, 5, 10, 'LEVEL_2', NOW(), NOW()),
(14, 1103, 6, 8, 'LEVEL_1', NOW(), NOW()),

-- 도우미 1104: 숙련자 다수
(15, 1104, 1, 9, 'LEVEL_1', NOW(), NOW()),
(16, 1104, 2, 8, 'LEVEL_1', NOW(), NOW()),
(17, 1104, 3, 7, 'LEVEL_1', NOW(), NOW()),
(18, 1104, 4, 6, 'LEVEL_1', NOW(), NOW()),
(19, 1104, 5, 5, 'LEVEL_1', NOW(), NOW()),

-- 도우미 1105: 경계값 테스트
(20, 1105, 1, 4, NULL, NOW(), NOW()),       -- 4회 (뱃지 없음)
(21, 1105, 2, 5, 'LEVEL_1', NOW(), NOW()),  -- 5회 (숙련자 경계)
(22, 1105, 3, 9, 'LEVEL_1', NOW(), NOW()),  -- 9회 (숙련자 경계)
(23, 1105, 4, 10, 'LEVEL_2', NOW(), NOW()), -- 10회 (전문가 경계)

-- 도우미 1106: 한 분야 집중
(24, 1106, 4, 25, 'LEVEL_2', NOW(), NOW()), -- 발달장애 고급 전문가

-- 도우미 1107~1109: 다양한 케이스
(25, 1107, 1, 6, 'LEVEL_1', NOW(), NOW()),
(26, 1107, 2, 11, 'LEVEL_2', NOW(), NOW()),

(27, 1108, 3, 14, 'LEVEL_2', NOW(), NOW()),
(28, 1108, 4, 3, NULL, NOW(), NOW()),

(29, 1109, 5, 7, 'LEVEL_1', NOW(), NOW()),
(30, 1109, 6, 13, 'LEVEL_2', NOW(), NOW())

ON DUPLICATE KEY UPDATE
    completion_count = VALUES(completion_count),
    badge_code = VALUES(badge_code),
    updated_at = NOW();
