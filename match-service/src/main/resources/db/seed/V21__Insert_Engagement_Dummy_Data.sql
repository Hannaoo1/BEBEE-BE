-- ========================================
-- Engagement 더미 데이터
-- ========================================
-- Agreement 10005 (Match 40005)의 기간 동안 월~금 날짜에 대한 Engagement 생성
-- 기간: CURDATE() ~ CURDATE() + 21일 (3주간)
-- 스케줄: 월~금

SET @match_id = 40005;
SET @start_date = CURDATE();
SET @end_date = DATE_ADD(CURDATE(), INTERVAL 21 DAY);
SET @engagement_id = 60001;

-- ========================================
-- 임시 테이블로 날짜 범위 생성 (월~금만)
-- ========================================
DROP TEMPORARY TABLE IF EXISTS temp_weekday_dates;

CREATE TEMPORARY TABLE temp_weekday_dates (
    date DATE,
    day_name VARCHAR(10)
);

-- 21일간의 모든 날짜 생성 후 월~금만 필터링
INSERT INTO temp_weekday_dates (date, day_name)
SELECT
    DATE_ADD(@start_date, INTERVAL seq DAY) AS date,
    DAYNAME(DATE_ADD(@start_date, INTERVAL seq DAY)) AS day_name
FROM (
    SELECT 0 AS seq UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL
    SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL
    SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL
    SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL
    SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL
    SELECT 20 UNION ALL SELECT 21
) AS seq_numbers
WHERE DAYOFWEEK(DATE_ADD(@start_date, INTERVAL seq DAY)) BETWEEN 2 AND 6  -- 월~금 (2=Monday, 6=Friday)
ORDER BY date;

-- ========================================
-- Engagement 데이터 삽입
-- ========================================

-- 동적으로 engagement_id 증가시키며 삽입
SET @row_number = 0;

INSERT INTO engagement (
    engagement_id,
    match_id,
    date,
    type,
    status,
    is_disabled_check,
    is_helper_check,
    created_at,
    updated_at
)
SELECT
    @engagement_id + (@row_number := @row_number + 1) - 1 AS engagement_id,
    @match_id AS match_id,
    date,
    'TERM' AS type,
    CASE
        WHEN date < CURDATE() THEN 'COMPLETED'
        WHEN date = CURDATE() THEN 'NOT_COMPLETED'
        ELSE 'NOT_COMPLETED'
    END AS status,
    CASE
        WHEN date < CURDATE() THEN TRUE
        ELSE FALSE
    END AS is_disabled_check,
    CASE
        WHEN date < CURDATE() THEN TRUE
        ELSE FALSE
    END AS is_helper_check,
    NOW() AS created_at,
    NOW() AS updated_at
FROM temp_weekday_dates
ORDER BY date;

-- 임시 테이블 삭제
DROP TEMPORARY TABLE IF EXISTS temp_weekday_dates;

-- ========================================
-- 확인 쿼리 (주석 해제하여 사용)
-- ========================================
-- SELECT
--     e.engagement_id,
--     e.match_id,
--     e.date,
--     DAYNAME(e.date) AS day_name,
--     e.type,
--     e.status,
--     e.is_disabled_check,
--     e.is_helper_check
-- FROM engagement e
-- WHERE e.match_id = 40005
-- ORDER BY e.date;