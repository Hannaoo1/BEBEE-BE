-- ========================================
-- Engagement 더미 데이터 (다양한 시나리오)
-- ========================================
-- 2026년 1월 기준 명확한 날짜로 Engagement 생성
--
-- 구성:
-- 1. 하루도움(DAY) 4개: 1월 특정 날짜에 1회성 활동
-- 2. 지속도움(TERM): 월/수, 화/목 스케줄로 주 2회
-- ========================================

-- ========================================
-- 하루도움(DAY) Agreement & Match & Engagement 추가
-- ========================================

-- DAY Agreement 1: 병원 동행 (1월 12일 일요일)
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10006, 1002, 800, 100, 8000, 8000, '서울시 강남구', 'DAY', '2026-01-05', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20006, 10006, '2026-01-12', '2026-01-12', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30013, 10006, 'SUNDAY', '09:00:00', '12:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10006, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40006, 800, 100, '일요일 병원 동행', 50006, 10006, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

-- DAY Engagement 1
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73001, 40006, '2026-01-12', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- DAY Agreement 2: 쇼핑 동행 (1월 17일 금요일)
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10007, 1004, 900, 200, 6000, 6000, '서울시 송파구', 'DAY', '2026-01-06', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20007, 10007, '2026-01-17', '2026-01-17', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30014, 10007, 'FRIDAY', '14:00:00', '17:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10007, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40007, 900, 200, '쇼핑 동행 도움', 50007, 10007, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

-- DAY Engagement 2
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73002, 40007, '2026-01-17', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- DAY Agreement 3: 관공서 방문 동행 (1월 23일 목요일)
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10008, 1005, 1000, 100, 7000, 7000, '서울시 마포구', 'DAY', '2026-01-07', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20008, 10008, '2026-01-23', '2026-01-23', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30015, 10008, 'THURSDAY', '10:00:00', '13:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10008, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40008, 1000, 100, '관공서 방문 도움', 50008, 10008, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

-- DAY Engagement 3
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73003, 40008, '2026-01-23', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- DAY Agreement 4: 문화행사 관람 동행 (1월 6일 화요일 - 과거, 완료됨)
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10009, 1006, 700, 200, 5000, 5000, '서울시 종로구', 'DAY', '2026-01-02', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20009, 10009, '2026-01-06', '2026-01-06', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30016, 10009, 'TUESDAY', '15:00:00', '18:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10009, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40009, 700, 200, '문화행사 관람 동행', 50009, 10009, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

-- DAY Engagement 4 (과거, 완료)
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73004, 40009, '2026-01-06', 'DAY', 'COMPLETED', TRUE, TRUE, NOW(), NOW());


-- ========================================
-- 지속도움(TERM) Engagements
-- ========================================

-- Match 40002: 문화센터 수업 동행 (월, 수 / 2주간)
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(70001, 40002, '2026-01-06', 'TERM', 'COMPLETED', TRUE, TRUE, NOW(), NOW()),
(70002, 40002, '2026-01-08', 'TERM', 'COMPLETED', TRUE, TRUE, NOW(), NOW()),
(70003, 40002, '2026-01-13', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(70004, 40002, '2026-01-15', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(70005, 40002, '2026-01-20', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(70006, 40002, '2026-01-22', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());

-- Match 40003: 정기 통원 치료 동행 (화, 목 / 1개월간)
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(71001, 40003, '2026-01-07', 'TERM', 'NOT_COMPLETED', FALSE, TRUE, NOW(), NOW()),
(71002, 40003, '2026-01-09', 'TERM', 'COMPLETED', TRUE, TRUE, NOW(), NOW()),
(71003, 40003, '2026-01-14', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(71004, 40003, '2026-01-16', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(71005, 40003, '2026-01-21', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(71006, 40003, '2026-01-23', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(71007, 40003, '2026-01-28', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(71008, 40003, '2026-01-30', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());

-- Match 40004: 주 2회 방문 목욕 서비스 (화, 목 / 2주간)
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(72001, 40004, '2026-01-07', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(72002, 40004, '2026-01-09', 'TERM', 'NOT_COMPLETED', FALSE, TRUE, NOW(), NOW()),
(72003, 40004, '2026-01-14', 'TERM', 'NOT_COMPLETED', TRUE, FALSE, NOW(), NOW()),
(72004, 40004, '2026-01-16', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(72005, 40004, '2026-01-21', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW()),
(72006, 40004, '2026-01-23', 'TERM', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());

-- ========================================
-- 확인 쿼리 (주석 해제하여 사용)
-- ========================================
-- SELECT
--     e.engagement_id,
--     e.match_id,
--     m.title AS match_title,
--     e.date,
--     DAYNAME(e.date) AS day_name,
--     e.type,
--     e.status,
--     e.is_disabled_check,
--     e.is_helper_check,
--     CASE
--         WHEN e.date < '2026-01-10' THEN '과거'
--         WHEN e.date = '2026-01-10' THEN '오늘'
--         ELSE '미래'
--     END AS date_category
-- FROM engagement e
-- JOIN `match` m ON e.match_id = m.match_id
-- WHERE e.match_id IN (40002, 40003, 40004, 40006, 40007, 40008, 40009)
-- ORDER BY e.type DESC, e.match_id, e.date;
