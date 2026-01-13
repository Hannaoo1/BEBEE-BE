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
--
-- 특징:
-- - 장애인: 100번 회원만
-- - 도우미: 700번, 800번 회원만
-- - 모든 체크 상태: FALSE
-- ========================================

-- ========================================
-- 1. 2026-01-01 (수요일) - 병원 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10020, 1002, 700, 100, 6000, 6000, '서울시 강남구', 'DAY', '2025-12-28', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20020, 10020, '2026-01-01', '2026-01-01', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30030, 10020, 'WEDNESDAY', '09:00:00', '12:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10020, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40020, 700, 100, '병원 동행 도움', 50020, 10020, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80011, 40020, '2026-01-01', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 2. 2026-01-02 (목요일) - 쇼핑 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10021, 1004, 800, 100, 5000, 5000, '서울시 송파구', 'DAY', '2025-12-29', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20021, 10021, '2026-01-02', '2026-01-02', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30031, 10021, 'THURSDAY', '14:00:00', '17:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10021, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40021, 800, 100, '쇼핑 동행 도움', 50021, 10021, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80012, 40021, '2026-01-02', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 3. 2026-01-03 (금요일) - 은행 업무 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10022, 1005, 700, 100, 7000, 7000, '서울시 마포구', 'DAY', '2025-12-30', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20022, 10022, '2026-01-03', '2026-01-03', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30032, 10022, 'FRIDAY', '10:00:00', '12:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10022, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40022, 700, 100, '은행 업무 동행', 50022, 10022, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80013, 40022, '2026-01-03', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 4. 2026-01-04 (토요일) - 문화센터 수업 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10023, 1003, 800, 100, 8000, 8000, '서울시 종로구', 'DAY', '2025-12-31', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20023, 10023, '2026-01-04', '2026-01-04', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30033, 10023, 'SATURDAY', '15:00:00', '18:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10023, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40023, 800, 100, '문화센터 수업 동행', 50023, 10023, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80014, 40023, '2026-01-04', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 5. 2026-01-05 (일요일) - 공원 산책 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10024, 1006, 700, 100, 4000, 4000, '서울시 강동구', 'DAY', '2026-01-01', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20024, 10024, '2026-01-05', '2026-01-05', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30034, 10024, 'SUNDAY', '10:00:00', '12:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10024, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40024, 700, 100, '공원 산책 동행', 50024, 10024, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80015, 40024, '2026-01-05', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 6. 2026-01-06 (월요일) - 관공서 방문 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10025, 1008, 800, 100, 6500, 6500, '서울시 동대문구', 'DAY', '2026-01-02', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20025, 10025, '2026-01-06', '2026-01-06', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30035, 10025, 'MONDAY', '09:00:00', '13:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10025, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40025, 800, 100, '관공서 방문 동행', 50025, 10025, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80016, 40025, '2026-01-06', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 7. 2026-01-07 (화요일) - 약국 방문 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10026, 1010, 700, 100, 3500, 3500, '서울시 도봉구', 'DAY', '2026-01-03', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20026, 10026, '2026-01-07', '2026-01-07', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30036, 10026, 'TUESDAY', '11:00:00', '13:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10026, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40026, 700, 100, '약국 방문 동행', 50026, 10026, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80017, 40026, '2026-01-07', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 8. 2026-01-08 (수요일) - 마트 장보기 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10027, 1011, 800, 100, 5500, 5500, '서울시 서초구', 'DAY', '2026-01-04', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20027, 10027, '2026-01-08', '2026-01-08', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30037, 10027, 'WEDNESDAY', '14:00:00', '16:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10027, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40027, 800, 100, '마트 장보기 동행', 50027, 10027, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80018, 40027, '2026-01-08', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 9. 2026-01-09 (목요일) - 미용실 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10028, 1012, 700, 100, 4500, 4500, '서울시 영등포구', 'DAY', '2026-01-05', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20028, 10028, '2026-01-09', '2026-01-09', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30038, 10028, 'THURSDAY', '10:00:00', '12:30:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10028, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40028, 700, 100, '미용실 동행', 50028, 10028, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80019, 40028, '2026-01-09', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());


-- ========================================
-- 10. 2026-01-10 (금요일) - 도서관 방문 동행
-- ========================================
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, status, is_volunteer, created_at, updated_at)
VALUES (10029, 1013, 800, 100, 4000, 4000, '서울시 관악구', 'DAY', '2026-01-06', 'CONFIRMED', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE status = VALUES(status), updated_at = NOW();

INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (20029, 10029, '2026-01-10', '2026-01-10', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_date = VALUES(start_date), end_date = VALUES(end_date), updated_at = NOW();

INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (30039, 10029, 'FRIDAY', '13:00:00', '16:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time), updated_at = NOW();

INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES (10029, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE category_name = VALUES(category_name), updated_at = NOW();

INSERT INTO `match` (match_id, helper_id, disabled_id, title, chat_room_id, agreement_id, image_url, created_at, updated_at)
VALUES (40029, 800, 100, '도서관 방문 동행', 50029, 10029, 'https://example.com/posts/dummy.jpg', NOW(), NOW())
ON DUPLICATE KEY UPDATE title = VALUES(title), updated_at = NOW();

INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (80020, 40029, '2026-01-10', 'DAY', 'NOT_COMPLETED', FALSE, FALSE, NOW(), NOW());