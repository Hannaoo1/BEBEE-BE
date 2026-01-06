-- ========================================
-- V6: MemberSync 데이터 업데이트
-- ========================================
-- 기존 회원 데이터를 실제 이름으로 업데이트하고 추가 회원 삽입 (총 10명)

-- 회원 1: 장애인 (김철수)
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    100,
    '김철수',
    'https://example.com/profiles/admin.jpg',
    100.00,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

-- 회원 2: 장애인 (김민수)
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    200,
    '김민수',
    'https://example.com/profiles/member200.jpg',
    85.5,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

-- 회원 3: 장애인 (이영희)
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    300,
    '이영희',
    'https://example.com/profiles/member300.jpg',
    92.0,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

-- 회원 4: 장애인 (박민수)
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    400,
    '박민수',
    'https://example.com/profiles/member400.jpg',
    78.3,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

-- 회원 5: 장애인 (최은정)
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    500,
    '최은정',
    'https://example.com/profiles/member500.jpg',
    88.7,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

-- 회원 6: 장애인 (정수진)
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    600,
    '정수진',
    'https://example.com/profiles/member600.jpg',
    81.2,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

-- 회원 7: 도우미 (강지훈)
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    700,
    '강지훈',
    'https://example.com/profiles/member700.jpg',
    95.5,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

-- 회원 8: 도우미 (윤서연)
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    800,
    '윤서연',
    'https://example.com/profiles/member800.jpg',
    97.8,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

-- 회원 9: 도우미 (임동현)
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    900,
    '임동현',
    'https://example.com/profiles/member900.jpg',
    93.2,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();

-- 회원 10: 도우미 (한미래)
INSERT INTO member_sync (member_id, nickname, profile_image_url, sweetness, created_at, updated_at)
VALUES (
    1000,
    '한미래',
    'https://example.com/profiles/member1000.jpg',
    96.4,
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    profile_image_url = VALUES(profile_image_url),
    sweetness = VALUES(sweetness),
    updated_at = NOW();