-- ========================================
-- Engagement 활동 완료 테스트용 더미 데이터
-- ========================================
-- BB-85: 활동 완료 API 및 스케줄러 테스트

-- ========================================
-- 사용 데이터 정보
-- ========================================
-- Agreement: 10001 (기존 더미 데이터 사용)
--   - post_id: 1001
--   - helper_id: 700 (도우미)
--   - disabled_id: 100 (장애인)
--   - type: DAY
--   - unit_honey: 5000

-- ========================================
-- 1. API 테스트용 (오늘 날짜)
-- ========================================

-- 9001: 케이스 1 테스트 (장애인 체크 → 즉시 완료)
INSERT INTO engagement (
    engagement_id,
    agreement_id,
    type,
    status,
    is_disabled_check,
    is_helper_check,
    activity_date,
    completed_count,
    created_at,
    updated_at
) VALUES (
    9001,
    10001,
    'DAY',
    'PENDING',
    FALSE,
    FALSE,
    CURDATE(),
    0,
    NOW(),
    NOW()
);

-- 9002: 케이스 2 테스트 (도우미 체크 → PENDING 유지)
INSERT INTO engagement (
    engagement_id,
    agreement_id,
    type,
    status,
    is_disabled_check,
    is_helper_check,
    activity_date,
    completed_count,
    created_at,
    updated_at
) VALUES (
    9002,
    10001,
    'DAY',
    'PENDING',
    FALSE,
    FALSE,
    CURDATE(),
    0,
    NOW(),
    NOW()
);

-- 9003: 순차 체크 테스트 (도우미 → 장애인)
INSERT INTO engagement (
    engagement_id,
    agreement_id,
    type,
    status,
    is_disabled_check,
    is_helper_check,
    activity_date,
    completed_count,
    created_at,
    updated_at
) VALUES (
    9003,
    10001,
    'DAY',
    'PENDING',
    FALSE,
    FALSE,
    CURDATE(),
    0,
    NOW(),
    NOW()
);

-- ========================================
-- 2. 스케줄러 테스트용
-- ========================================

-- 9010: 케이스 3 (도우미만 체크, 3일 전 → 자동 완료 대상)
INSERT INTO engagement (
    engagement_id,
    agreement_id,
    type,
    status,
    is_disabled_check,
    is_helper_check,
    activity_date,
    completed_count,
    created_at,
    updated_at
) VALUES (
    9010,
    10001,
    'DAY',
    'PENDING',
    FALSE,
    TRUE,
    CURDATE() - INTERVAL 3 DAY,
    0,
    NOW(),
    NOW()
);

-- 9011: 케이스 4 (둘 다 미체크, 3일 전 → 미완료 대상)
INSERT INTO engagement (
    engagement_id,
    agreement_id,
    type,
    status,
    is_disabled_check,
    is_helper_check,
    activity_date,
    completed_count,
    created_at,
    updated_at
) VALUES (
    9011,
    10001,
    'DAY',
    'PENDING',
    FALSE,
    FALSE,
    CURDATE() - INTERVAL 3 DAY,
    0,
    NOW(),
    NOW()
);

-- 9012: 경고 대상 (둘 다 미체크, 2일 전)
INSERT INTO engagement (
    engagement_id,
    agreement_id,
    type,
    status,
    is_disabled_check,
    is_helper_check,
    activity_date,
    completed_count,
    created_at,
    updated_at
) VALUES (
    9012,
    10001,
    'DAY',
    'PENDING',
    FALSE,
    FALSE,
    CURDATE() - INTERVAL 2 DAY,
    0,
    NOW(),
    NOW()
);

-- ========================================
-- 3. 참고용 (다양한 상태)
-- ========================================

-- 9020: 이미 완료된 활동 (멱등성 테스트용)
INSERT INTO engagement (
    engagement_id,
    agreement_id,
    type,
    status,
    is_disabled_check,
    is_helper_check,
    activity_date,
    completed_count,
    created_at,
    updated_at
) VALUES (
    9020,
    10001,
    'DAY',
    'COMPLETED',
    TRUE,
    FALSE,
    CURDATE() - INTERVAL 1 DAY,
    1,
    NOW(),
    NOW()
);

-- 9021: 미완료 상태 (참고용)
INSERT INTO engagement (
    engagement_id,
    agreement_id,
    type,
    status,
    is_disabled_check,
    is_helper_check,
    activity_date,
    completed_count,
    created_at,
    updated_at
) VALUES (
    9021,
    10001,
    'DAY',
    'INCOMPLETED',
    FALSE,
    FALSE,
    CURDATE() - INTERVAL 5 DAY,
    0,
    NOW(),
    NOW()
);
