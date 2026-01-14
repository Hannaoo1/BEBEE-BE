-- ========================================
-- V110: 회원별 장애 유형 더미 데이터 삽입
-- ========================================
-- 장애인(DISABLED) 회원에게 장애 유형 1개씩 할당
-- 장애 유형:
--   1: 지체장애 (PHYSICAL)
--   2: 시각장애 (VISUAL)
--   3: 청각장애 (HEARING)
--   4: 발달장애 (DEVELOPMENTAL)
--   5: 내부기관장애 (INTERNAL_ORGAN)
--   6: 기타장애 (ETC)

-- 회원 100 (김철수) - 지체장애
INSERT INTO member_disability_category_sync (member_id, disability_category_id, created_at, updated_at)
VALUES (100, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 회원 200 (김민수) - 시각장애
INSERT INTO member_disability_category_sync (member_id, disability_category_id, created_at, updated_at)
VALUES (200, 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 회원 300 (이영희) - 청각장애
INSERT INTO member_disability_category_sync (member_id, disability_category_id, created_at, updated_at)
VALUES (300, 3, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 회원 400 (박민수) - 발달장애
INSERT INTO member_disability_category_sync (member_id, disability_category_id, created_at, updated_at)
VALUES (400, 4, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 회원 500 (최은정) - 내부기관장애
INSERT INTO member_disability_category_sync (member_id, disability_category_id, created_at, updated_at)
VALUES (500, 5, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 회원 600 (정수진) - 기타장애
INSERT INTO member_disability_category_sync (member_id, disability_category_id, created_at, updated_at)
VALUES (600, 6, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();