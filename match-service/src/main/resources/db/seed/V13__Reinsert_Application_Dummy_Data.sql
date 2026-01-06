-- ========================================
-- Application 더미 데이터
-- ========================================
-- /helper-applications/applicants 엔드포인트 테스트를 위한 지원 데이터
-- flyway 버전 충돌 이슈로 V10 내용을 새 버전으로 재적용

-- ========================================
-- Post 1001 (김철수, 병원 동행)에 대한 지원자들
-- ========================================

-- 지원자 1: 강지훈 (700) - 유료 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50001,
    1001,
    700,
    FALSE,
    NOW() - INTERVAL 2 HOUR,
    NOW() - INTERVAL 2 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- 지원자 2: 윤서연 (800) - 나눔 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50002,
    1001,
    800,
    TRUE,
    NOW() - INTERVAL 1 HOUR,
    NOW() - INTERVAL 1 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- 지원자 3: 임동현 (900) - 유료 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50003,
    1001,
    900,
    FALSE,
    NOW() - INTERVAL 30 MINUTE,
    NOW() - INTERVAL 30 MINUTE
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- ========================================
-- Post 1002 (김민수, 주말 공원 산책)에 대한 지원자들
-- ========================================

-- 지원자 1: 한미래 (1000) - 나눔 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50004,
    1002,
    1000,
    TRUE,
    NOW() - INTERVAL 5 HOUR,
    NOW() - INTERVAL 5 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- 지원자 2: 강지훈 (700) - 유료 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50005,
    1002,
    700,
    FALSE,
    NOW() - INTERVAL 4 HOUR,
    NOW() - INTERVAL 4 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- ========================================
-- Post 1003 (김철수, 문화센터 수업 동행)에 대한 지원자들
-- ========================================

-- 지원자 1: 윤서연 (800) - 유료 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50006,
    1003,
    800,
    FALSE,
    NOW() - INTERVAL 10 HOUR,
    NOW() - INTERVAL 10 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- 지원자 2: 임동현 (900) - 나눔 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50007,
    1003,
    900,
    TRUE,
    NOW() - INTERVAL 8 HOUR,
    NOW() - INTERVAL 8 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- 지원자 3: 한미래 (1000) - 유료 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50008,
    1003,
    1000,
    FALSE,
    NOW() - INTERVAL 6 HOUR,
    NOW() - INTERVAL 6 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- 지원자 4: 강지훈 (700) - 유료 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50009,
    1003,
    700,
    FALSE,
    NOW() - INTERVAL 3 HOUR,
    NOW() - INTERVAL 3 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- ========================================
-- Post 1004 (이영희, 마트 장보기)에 대한 지원자들
-- ========================================

-- 지원자 1: 윤서연 (800) - 나눔 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50010,
    1004,
    800,
    TRUE,
    NOW() - INTERVAL 12 HOUR,
    NOW() - INTERVAL 12 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- ========================================
-- Post 1005 (김민수, 도서관 이용 도움)에 대한 지원자들
-- ========================================

-- 지원자 1: 강지훈 (700) - 나눔 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50011,
    1005,
    700,
    TRUE,
    NOW() - INTERVAL 1 DAY,
    NOW() - INTERVAL 1 DAY
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- 지원자 2: 한미래 (1000) - 유료 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50012,
    1005,
    1000,
    FALSE,
    NOW() - INTERVAL 20 HOUR,
    NOW() - INTERVAL 20 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- 지원자 3: 임동현 (900) - 유료 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50013,
    1005,
    900,
    FALSE,
    NOW() - INTERVAL 18 HOUR,
    NOW() - INTERVAL 18 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- ========================================
-- Post 1011 (김민수, 주 2회 방문 목욕)에 대한 지원자들
-- ========================================

-- 지원자 1: 임동현 (900) - 유료 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50014,
    1011,
    900,
    FALSE,
    NOW() - INTERVAL 3 DAY,
    NOW() - INTERVAL 3 DAY
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- 지원자 2: 한미래 (1000) - 나눔 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50015,
    1011,
    1000,
    TRUE,
    NOW() - INTERVAL 2 DAY - INTERVAL 18 HOUR,
    NOW() - INTERVAL 2 DAY - INTERVAL 18 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- ========================================
-- Post 1021 (김철수, 당뇨 환자 혈당 체크)에 대한 지원자들
-- ========================================

-- 지원자 1: 윤서연 (800) - 유료 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50016,
    1021,
    800,
    FALSE,
    NOW() - INTERVAL 7 DAY,
    NOW() - INTERVAL 7 DAY
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();

-- 지원자 2: 강지훈 (700) - 나눔 지원
INSERT INTO application (application_id, post_id, applicant_id, is_volunteer, created_at, updated_at)
VALUES (
    50017,
    1021,
    700,
    TRUE,
    NOW() - INTERVAL 6 DAY - INTERVAL 12 HOUR,
    NOW() - INTERVAL 6 DAY - INTERVAL 12 HOUR
) ON DUPLICATE KEY UPDATE
    is_volunteer = VALUES(is_volunteer),
    updated_at = NOW();