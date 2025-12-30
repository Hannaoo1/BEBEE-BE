-- ========================================
-- Agreement, Match 더미 데이터
-- ========================================
-- GetMatchesByDateUseCase 테스트를 위한 매칭 데이터

-- ========================================
-- Agreement (매칭 확인서)
-- ========================================

-- Agreement 1: Post 1001 (병원 동행) - DAY 타입
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, is_day_complete, is_term_complete, status, is_volunteer, created_at, updated_at)
VALUES (
    10001,
    1001,
    101,  -- HELPER 회원
    100,  -- DISABLED 회원
    5000,
    5000,
    '서울시 강남구 역삼동',
    'DAY',
    CURDATE(),
    FALSE,
    FALSE,
    'CONFIRMED',
    FALSE,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    status = VALUES(status),
    updated_at = NOW();

-- Agreement 2: Post 1003 (문화센터 수업 동행) - TERM 타입
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, is_day_complete, is_term_complete, status, is_volunteer, created_at, updated_at)
VALUES (
    10002,
    1003,
    102,
    100,
    4000,
    40000,
    '서울시 송파구 잠실동',
    'TERM',
    CURDATE() - INTERVAL 1 DAY,
    FALSE,
    FALSE,
    'CONFIRMED',
    FALSE,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    status = VALUES(status),
    updated_at = NOW();

-- Agreement 3: Post 1008 (정기 통원 치료 동행) - TERM 타입, 나눔
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, is_day_complete, is_term_complete, status, is_volunteer, created_at, updated_at)
VALUES (
    10003,
    1008,
    103,
    200,
    0,      -- 나눔이므로 0
    0,
    '서울시 동대문구 회기동',
    'TERM',
    CURDATE() - INTERVAL 2 DAY,
    FALSE,
    FALSE,
    'CONFIRMED',
    TRUE,   -- 나눔
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    status = VALUES(status),
    updated_at = NOW();

-- Agreement 4: Post 1011 (주 2회 방문 목욕 서비스) - TERM 타입
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, is_day_complete, is_term_complete, status, is_volunteer, created_at, updated_at)
VALUES (
    10004,
    1011,
    104,
    200,
    8000,
    80000,
    '서울시 도봉구 쌍문동',
    'TERM',
    CURDATE() - INTERVAL 3 DAY,
    FALSE,
    FALSE,
    'CONFIRMED',
    FALSE,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    status = VALUES(status),
    updated_at = NOW();

-- Agreement 5: Post 1021 (당뇨 환자 혈당 체크) - TERM 타입
INSERT INTO agreement (agreement_id, post_id, helper_id, disabled_id, unit_honey, total_honey, region, type, confirmation_date, is_day_complete, is_term_complete, status, is_volunteer, created_at, updated_at)
VALUES (
    10005,
    1021,
    101,
    100,
    7000,
    70000,
    '서울시 동작구 노량진동',
    'TERM',
    CURDATE() - INTERVAL 5 DAY,
    FALSE,
    FALSE,
    'CONFIRMED',
    FALSE,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    status = VALUES(status),
    updated_at = NOW();

-- ========================================
-- AgreementPeriod (기간 정보)
-- ========================================

-- AgreementPeriod 1: Agreement 10001 (오늘)
INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (
    20001,
    10001,
    CURDATE(),
    CURDATE(),
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    updated_at = NOW();

-- AgreementPeriod 2: Agreement 10002 (2주간)
INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (
    20002,
    10002,
    CURDATE(),
    CURDATE() + INTERVAL 14 DAY,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    updated_at = NOW();

-- AgreementPeriod 3: Agreement 10003 (1개월간)
INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (
    20003,
    10003,
    CURDATE(),
    CURDATE() + INTERVAL 30 DAY,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    updated_at = NOW();

-- AgreementPeriod 4: Agreement 10004 (2주간)
INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (
    20004,
    10004,
    CURDATE(),
    CURDATE() + INTERVAL 14 DAY,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    updated_at = NOW();

-- AgreementPeriod 5: Agreement 10005 (3주간)
INSERT INTO agreement_period (agreement_period_id, agreement_id, start_date, end_date, created_at, updated_at)
VALUES (
    20005,
    10005,
    CURDATE(),
    CURDATE() + INTERVAL 21 DAY,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    start_date = VALUES(start_date),
    end_date = VALUES(end_date),
    updated_at = NOW();

-- ========================================
-- AgreementSchedule (스케줄 정보)
-- ========================================

-- Agreement 10001 스케줄 (DAY - 오늘만)
INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES (
    30001,
    10001,
    'MONDAY',
    '09:00:00',
    '12:00:00',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    start_time = VALUES(start_time),
    end_time = VALUES(end_time),
    updated_at = NOW();

-- Agreement 10002 스케줄 (TERM - 월, 수)
INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES
(30002, 10002, 'MONDAY', '14:00:00', '16:00:00', NOW(), NOW()),
(30003, 10002, 'WEDNESDAY', '14:00:00', '16:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    start_time = VALUES(start_time),
    end_time = VALUES(end_time),
    updated_at = NOW();

-- Agreement 10003 스케줄 (TERM - 화, 목)
INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES
(30004, 10003, 'TUESDAY', '10:00:00', '11:30:00', NOW(), NOW()),
(30005, 10003, 'THURSDAY', '10:00:00', '11:30:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    start_time = VALUES(start_time),
    end_time = VALUES(end_time),
    updated_at = NOW();

-- Agreement 10004 스케줄 (TERM - 화, 목)
INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES
(30006, 10004, 'TUESDAY', '18:00:00', '20:00:00', NOW(), NOW()),
(30007, 10004, 'THURSDAY', '18:00:00', '20:00:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    start_time = VALUES(start_time),
    end_time = VALUES(end_time),
    updated_at = NOW();

-- Agreement 10005 스케줄 (TERM - 매일)
INSERT INTO agreement_schedule (agreement_schedule_id, agreement_id, day_of_week, start_time, end_time, created_at, updated_at)
VALUES
(30008, 10005, 'MONDAY', '08:00:00', '08:30:00', NOW(), NOW()),
(30009, 10005, 'TUESDAY', '08:00:00', '08:30:00', NOW(), NOW()),
(30010, 10005, 'WEDNESDAY', '08:00:00', '08:30:00', NOW(), NOW()),
(30011, 10005, 'THURSDAY', '08:00:00', '08:30:00', NOW(), NOW()),
(30012, 10005, 'FRIDAY', '08:00:00', '08:30:00', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    start_time = VALUES(start_time),
    end_time = VALUES(end_time),
    updated_at = NOW();

-- ========================================
-- Agreement_HelpCategory (도움 카테고리)
-- ========================================

-- Agreement 10001 카테고리 (외출동행)
INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES
(10001, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    category_name = VALUES(category_name),
    updated_at = NOW();

-- Agreement 10002 카테고리 (외출동행)
INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES
(10002, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    category_name = VALUES(category_name),
    updated_at = NOW();

-- Agreement 10003 카테고리 (외출동행)
INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES
(10003, 1, '외출동행', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    category_name = VALUES(category_name),
    updated_at = NOW();

-- Agreement 10004 카테고리 (방문목욕)
INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES
(10004, 2, '방문목욕', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    category_name = VALUES(category_name),
    updated_at = NOW();

-- Agreement 10005 카테고리 (방문간호)
INSERT INTO agreement_help_category (agreement_id, help_category_id, category_name, created_at, updated_at)
VALUES
(10005, 3, '방문간호', NOW(), NOW())
ON DUPLICATE KEY UPDATE
    category_name = VALUES(category_name),
    updated_at = NOW();

-- ========================================
-- Match (매칭)
-- ========================================

-- Match 1: Agreement 10001
INSERT INTO `match` (match_id, helper_id, disabled_id, post_id, title, chat_room_id, agreement_id, created_at, updated_at)
VALUES (
    40001,
    101,
    100,
    1001,
    '병원 동행 도와주실 분',
    50001,  -- 채팅방 ID
    10001,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    chat_room_id = VALUES(chat_room_id),
    updated_at = NOW();

-- Match 2: Agreement 10002
INSERT INTO `match` (match_id, helper_id, disabled_id, post_id, title, chat_room_id, agreement_id, created_at, updated_at)
VALUES (
    40002,
    102,
    100,
    1003,
    '문화센터 수업 동행',
    50002,
    10002,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    chat_room_id = VALUES(chat_room_id),
    updated_at = NOW();

-- Match 3: Agreement 10003
INSERT INTO `match` (match_id, helper_id, disabled_id, post_id, title, chat_room_id, agreement_id, created_at, updated_at)
VALUES (
    40003,
    103,
    200,
    1008,
    '정기 통원 치료 동행',
    50003,
    10003,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    chat_room_id = VALUES(chat_room_id),
    updated_at = NOW();

-- Match 4: Agreement 10004
INSERT INTO `match` (match_id, helper_id, disabled_id, post_id, title, chat_room_id, agreement_id, created_at, updated_at)
VALUES (
    40004,
    104,
    200,
    1011,
    '주 2회 방문 목욕 서비스',
    50004,
    10004,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    chat_room_id = VALUES(chat_room_id),
    updated_at = NOW();

-- Match 5: Agreement 10005
INSERT INTO `match` (match_id, helper_id, disabled_id, post_id, title, chat_room_id, agreement_id, created_at, updated_at)
VALUES (
    40005,
    101,
    100,
    1021,
    '당뇨 환자 혈당 체크',
    50005,
    10005,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    title = VALUES(title),
    chat_room_id = VALUES(chat_room_id),
    updated_at = NOW();
