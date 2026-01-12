-- ========================================
-- 하루도움(DAY) 지난 날짜 Engagement 더미 데이터
-- ========================================
-- 2026-01-01 ~ 2026-01-10 기간의 하루도움 활동 10개 생성
--
-- 구성:
-- - Agreement (DAY 타입)
-- - AgreementPeriod (1일)
-- - AgreementSchedule
-- - AgreementHelpCategory
-- - Match
-- - Engagement (지난 날짜)
-- ========================================

-- ========================================
-- 1. 2026-01-01 (수요일) - 병원 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10010, 1002, 700, 100, 6000, 6000, '서울시 강남구', 'DAY', '2025-12-28', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20010, 10010, '2026-01-01', '2026-01-01', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30020, 10010, 'WEDNESDAY', '09:00:00', '12:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10010, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40010, 700, 100, '병원 동행 도움', 50010, 10010, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80001, 40010, '2026-01-01', 'DAY', 'NOT_COMPLETED', TRUE, TRUE, NOW(), NOW());


-- ========================================
-- 2. 2026-01-02 (목요일) - 쇼핑 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10011, 1004, 800, 200, 5000, 5000, '서울시 송파구', 'DAY', '2025-12-29', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20011, 10011, '2026-01-02', '2026-01-02', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30021, 10011, 'THURSDAY', '14:00:00', '17:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10011, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40011, 800, 200, '쇼핑 동행 도움', 50011, 10011, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80002, 40011, '2026-01-02', 'DAY', 'NOT_COMPLETED', FALSE, TRUE, NOW(), NOW());


-- ========================================
-- 3. 2026-01-03 (금요일) - 은행 업무 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10012, 1005, 900, 100, 7000, 7000, '서울시 마포구', 'DAY', '2025-12-30', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20012, 10012, '2026-01-03', '2026-01-03', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30022, 10012, 'FRIDAY', '10:00:00', '12:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10012, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40012, 900, 100, '은행 업무 동행', 50012, 10012, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80003, 40012, '2026-01-03', 'DAY', 'NOT_COMPLETED', TRUE, FALSE, NOW(), NOW());


-- ========================================
-- 4. 2026-01-04 (토요일) - 문화센터 수업 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10013, 1003, 1000, 200, 8000, 8000, '서울시 종로구', 'DAY', '2025-12-31', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20013, 10013, '2026-01-04', '2026-01-04', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30023, 10013, 'SATURDAY', '15:00:00', '18:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10013, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40013, 1000, 200, '문화센터 수업 동행', 50013, 10013, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80004, 40013, '2026-01-04', 'DAY', 'NOT_COMPLETED', TRUE, TRUE, NOW(), NOW());


-- ========================================
-- 5. 2026-01-05 (일요일) - 공원 산책 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10014, 1006, 700, 200, 4000, 4000, '서울시 강동구', 'DAY', '2026-01-01', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20014, 10014, '2026-01-05', '2026-01-05', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30024, 10014, 'SUNDAY', '10:00:00', '12:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10014, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40014, 700, 200, '공원 산책 동행', 50014, 10014, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80005, 40014, '2026-01-05', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 6. 2026-01-06 (월요일) - 관공서 방문 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10015, 1008, 800, 100, 6500, 6500, '서울시 동대문구', 'DAY', '2026-01-02', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20015, 10015, '2026-01-06', '2026-01-06', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30025, 10015, 'MONDAY', '09:00:00', '13:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10015, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40015, 800, 100, '관공서 방문 동행', 50015, 10015, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80006, 40015, '2026-01-06', 'DAY', 'NOT_COMPLETED', TRUE, FALSE, NOW(), NOW());


-- ========================================
-- 7. 2026-01-07 (화요일) - 약국 방문 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10016, 1010, 900, 200, 3500, 3500, '서울시 도봉구', 'DAY', '2026-01-03', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20016, 10016, '2026-01-07', '2026-01-07', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30026, 10016, 'TUESDAY', '11:00:00', '13:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10016, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40016, 900, 200, '약국 방문 동행', 50016, 10016, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80007, 40016, '2026-01-07', 'DAY', 'NOT_COMPLETED', FALSE, TRUE, NOW(), NOW());


-- ========================================
-- 8. 2026-01-08 (수요일) - 마트 장보기 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10017, 1011, 1000, 100, 5500, 5500, '서울시 서초구', 'DAY', '2026-01-04', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20017, 10017, '2026-01-08', '2026-01-08', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30027, 10017, 'WEDNESDAY', '14:00:00', '16:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10017, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40017, 1000, 100, '마트 장보기 동행', 50017, 10017, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80008, 40017, '2026-01-08', 'DAY', 'NOT_COMPLETED', TRUE, TRUE, NOW(), NOW());


-- ========================================
-- 9. 2026-01-09 (목요일) - 미용실 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10018, 1012, 700, 200, 4500, 4500, '서울시 영등포구', 'DAY', '2026-01-05', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20018, 10018, '2026-01-09', '2026-01-09', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30028, 10018, 'THURSDAY', '10:00:00', '12:30:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10018, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40018, 700, 200, '미용실 동행', 50018, 10018, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80009, 40018, '2026-01-09', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 10. 2026-01-10 (금요일) - 도서관 방문 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10019, 1013, 800, 100, 4000, 4000, '서울시 관악구', 'DAY', '2026-01-06', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20019, 10019, '2026-01-10', '2026-01-10', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30029, 10019, 'FRIDAY', '13:00:00', '16:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10019, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40019, 800, 100, '도서관 방문 동행', 50019, 10019, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80010, 40019, '2026-01-10', 'DAY', 'NOT_COMPLETED', TRUE, FALSE, NOW(), NOW());