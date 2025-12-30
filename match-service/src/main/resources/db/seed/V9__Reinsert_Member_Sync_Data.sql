-- ========================================
-- V9: MemberSync 데이터 재삽입
-- ========================================
-- birthDate를 포함한 회원 동기화 데이터 (총 10명)

-- 회원 1: 장애인 (김철수, 60대)
INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES (
    100,
    '김철수',
    'MALE',
    'DISABLED',
    '1960-05-15',
    37.5012,
    127.0396,
    'https://example.com/profiles/admin.jpg',
    '서울특별시 강남구 역삼동',
    '1168010500',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    birth_date = VALUES(birth_date),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    address_road = VALUES(address_road),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 2: 장애인 (김민수, 70대)
INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES (
    200,
    '김민수',
    'MALE',
    'DISABLED',
    '1955-08-20',
    37.4837,
    127.0324,
    'https://example.com/profiles/member200.jpg',
    '서울특별시 송파구 잠실동',
    '1165010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    birth_date = VALUES(birth_date),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    address_road = VALUES(address_road),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 3: 장애인 (이영희, 60대)
INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES (
    300,
    '이영희',
    'FEMALE',
    'DISABLED',
    '1965-03-10',
    37.5133,
    127.1000,
    'https://example.com/profiles/member300.jpg',
    '서울특별시 광진구 광장동',
    '1171010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    birth_date = VALUES(birth_date),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    address_road = VALUES(address_road),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 4: 장애인 (박민수, 50대)
INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES (
    400,
    '박민수',
    'MALE',
    'DISABLED',
    '1970-11-25',
    37.5388,
    127.1236,
    'https://example.com/profiles/member400.jpg',
    '서울특별시 광진구 구의동',
    '1174010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    birth_date = VALUES(birth_date),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    address_road = VALUES(address_road),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 5: 장애인 (최은정, 60대)
INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES (
    500,
    '최은정',
    'FEMALE',
    'DISABLED',
    '1958-07-30',
    37.5443,
    127.0557,
    'https://example.com/profiles/member500.jpg',
    '서울특별시 서대문구 연희동',
    '1120010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    birth_date = VALUES(birth_date),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    address_road = VALUES(address_road),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 6: 장애인 (정수진, 50대)
INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES (
    600,
    '정수진',
    'NONE',
    'DISABLED',
    '1975-02-14',
    37.5833,
    127.0913,
    'https://example.com/profiles/member600.jpg',
    '서울특별시 동대문구 청량리동',
    '1126010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    birth_date = VALUES(birth_date),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    address_road = VALUES(address_road),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 7: 도우미 (강지훈, 30대)
INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES (
    700,
    '강지훈',
    'MALE',
    'HELPER',
    '1990-09-05',
    37.5894,
    127.0582,
    'https://example.com/profiles/member700.jpg',
    '서울특별시 성북구 삼선동',
    '1123010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    birth_date = VALUES(birth_date),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    address_road = VALUES(address_road),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 8: 도우미 (윤서연, 30대)
INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES (
    800,
    '윤서연',
    'FEMALE',
    'HELPER',
    '1995-12-18',
    37.6106,
    127.0099,
    'https://example.com/profiles/member800.jpg',
    '서울특별시 종로구 평창동',
    '1117010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    birth_date = VALUES(birth_date),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    address_road = VALUES(address_road),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 9: 도우미 (임동현, 30대)
INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES (
    900,
    '임동현',
    'MALE',
    'HELPER',
    '1988-04-22',
    37.6388,
    127.0255,
    'https://example.com/profiles/member900.jpg',
    '서울특별시 은평구 응암동',
    '1132010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    birth_date = VALUES(birth_date),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    address_road = VALUES(address_road),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();

-- 회원 10: 도우미 (한미래, 30대)
INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES (
    1000,
    '한미래',
    'FEMALE',
    'HELPER',
    '1992-06-08',
    37.6500,
    127.0322,
    'https://example.com/profiles/member1000.jpg',
    '서울특별시 도봉구 방학동',
    '1135010100',
    NOW(),
    NOW()
) ON DUPLICATE KEY UPDATE
    nickname = VALUES(nickname),
    gender = VALUES(gender),
    role = VALUES(role),
    birth_date = VALUES(birth_date),
    latitude = VALUES(latitude),
    longitude = VALUES(longitude),
    profile_image_url = VALUES(profile_image_url),
    address_road = VALUES(address_road),
    legal_dong_code = VALUES(legal_dong_code),
    updated_at = NOW();