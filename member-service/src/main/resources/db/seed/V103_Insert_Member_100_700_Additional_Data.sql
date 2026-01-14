-- ========================================
-- V13: 회원 100, 700 추가 데이터 삽입
-- ========================================
-- 1. 회원 100 (장애인): 장애 정보 추가
-- 2. 회원 700 (도우미): 서류 정보 추가
-- 3. 회원 100, 700: 도움 카테고리 추가

-- ========================================
-- 1. 회원 100 (김철수) - 장애 정보 추가
-- ========================================
-- 시각장애 2급, 양 눈 빛 감지는 가능
INSERT INTO member_disability_category (
    member_id,
    disability_category_id,
    level,
    disability_description,
    created_at,
    updated_at
) VALUES (
    100,
    2,  -- 시각장애
    '2',
    '양 눈 빛 감지는 가능해요',
    NOW(),
    NOW()
);

-- ========================================
-- 2. 회원 700 (강지훈) - 서류 정보 추가
-- ========================================
-- 도우미 자격증 및 이수증 2개
INSERT INTO document (
    document_id,
    member_id,
    target_role,
    doc_code,
    doc_name_ko,
    description,
    created_at,
    updated_at
) VALUES
(
    10001,
    700,
    'HELPER',
    'DOC_HELPER_CERT_001',
    '요양보호사 자격증',
    '요양보호사 1급 자격증',
    NOW(),
    NOW()
),
(
    10002,
    700,
    'HELPER',
    'DOC_HELPER_CERT_002',
    '응급처치 교육 이수증',
    '응급처치 및 심폐소생술 교육 이수',
    NOW(),
    NOW()
);

-- ========================================
-- 3. 회원 100, 700 - 도움 카테고리 추가
-- ========================================
-- 회원 100 (장애인): 외출 동행(1), 가사 지원(4)
INSERT INTO member_help_category (
    member_id,
    help_category_id,
    created_at,
    updated_at
) VALUES
(
    100,
    1,  -- 외출 동행
    NOW(),
    NOW()
),
(
    100,
    4,  -- 가사 지원
    NOW(),
    NOW()
);

-- 회원 700 (도우미): 방문 목욕(2), 방문 간호(3)
INSERT INTO member_help_category (
    member_id,
    help_category_id,
    created_at,
    updated_at
) VALUES
(
    700,
    2,  -- 방문 목욕
    NOW(),
    NOW()
),
(
    700,
    3,  -- 방문 간호
    NOW(),
    NOW()
);