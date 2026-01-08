-- ========================================
-- V18: 1~5km 반경 내 도우미(HELPER) 더미 데이터 50개 추가
-- ========================================
-- 기준: 회원 100 (37.5012, 127.0396)
-- ID 시작: 1100

INSERT INTO member_sync (member_id, nickname, gender, role, birth_date, latitude, longitude, profile_image_url, address_road, legal_dong_code, created_at, updated_at)
VALUES
(1100, '도우미1100', 'MALE', 'HELPER', '1990-01-01', 37.5102, 127.0450, 'https://example.com/profiles/dummy.jpg', '서울 강남구 역삼동', '1168010100', NOW(), NOW()),
(1101, '도우미1101', 'FEMALE', 'HELPER', '1995-05-05', 37.4922, 127.0342, 'https://example.com/profiles/dummy.jpg', '서울 강남구 도곡동', '1168011800', NOW(), NOW()),
(1102, '도우미1102', 'MALE', 'HELPER', '1988-08-15', 37.5212, 127.0296, 'https://example.com/profiles/dummy.jpg', '서울 강남구 논현동', '1168010800', NOW(), NOW()),
(1103, '도우미1103', 'FEMALE', 'HELPER', '1992-12-25', 37.4812, 127.0496, 'https://example.com/profiles/dummy.jpg', '서울 강남구 개포동', '1168010300', NOW(), NOW()),
(1104, '도우미1104', 'MALE', 'HELPER', '1994-03-01', 37.5312, 127.0196, 'https://example.com/profiles/dummy.jpg', '서울 서초구 잠원동', '1165010600', NOW(), NOW()),
(1105, '도우미1105', 'FEMALE', 'HELPER', '1991-07-07', 37.4712, 127.0596, 'https://example.com/profiles/dummy.jpg', '서울 강남구 일원동', '1168011400', NOW(), NOW()),
(1106, '도우미1106', 'MALE', 'HELPER', '1985-11-11', 37.5412, 127.0096, 'https://example.com/profiles/dummy.jpg', '서울 용산구 한남동', '1117013100', NOW(), NOW()),
(1107, '도우미1107', 'FEMALE', 'HELPER', '1993-02-28', 37.4612, 127.0696, 'https://example.com/profiles/dummy.jpg', '서울 강남구 수서동', '1168011500', NOW(), NOW()),
(1108, '도우미1108', 'MALE', 'HELPER', '1996-09-09', 37.5512, 126.9996, 'https://example.com/profiles/dummy.jpg', '서울 중구 장충동', '1114014300', NOW(), NOW()),
(1109, '도우미1109', 'FEMALE', 'HELPER', '1990-04-04', 37.4512, 127.0796, 'https://example.com/profiles/dummy.jpg', '서울 송파구 문정동', '1171010800', NOW(), NOW()),
(1110, '도우미1110', 'MALE', 'HELPER', '1992-06-06', 37.5052, 127.0436, 'https://example.com/profiles/dummy.jpg', '서울 강남구 삼성동', '1168010500', NOW(), NOW()),
(1111, '도우미1111', 'FEMALE', 'HELPER', '1994-10-10', 37.4972, 127.0356, 'https://example.com/profiles/dummy.jpg', '서울 강남구 역삼동', '1168010100', NOW(), NOW()),
(1112, '도우미1112', 'MALE', 'HELPER', '1989-01-20', 37.5152, 127.0336, 'https://example.com/profiles/dummy.jpg', '서울 강남구 논현동', '1168010800', NOW(), NOW()),
(1113, '도우미1113', 'FEMALE', 'HELPER', '1995-12-12', 37.4872, 127.0456, 'https://example.com/profiles/dummy.jpg', '서울 강남구 도곡동', '1168011800', NOW(), NOW()),
(1114, '도우미1114', 'MALE', 'HELPER', '1991-05-25', 37.5252, 127.0236, 'https://example.com/profiles/dummy.jpg', '서울 강남구 신사동', '1168010700', NOW(), NOW()),
(1115, '도우미1115', 'FEMALE', 'HELPER', '1993-08-30', 37.4772, 127.0556, 'https://example.com/profiles/dummy.jpg', '서울 강남구 개포동', '1168010300', NOW(), NOW()),
(1116, '도우미1116', 'MALE', 'HELPER', '1987-03-15', 37.5352, 127.0136, 'https://example.com/profiles/dummy.jpg', '서울 용산구 이태원동', '1117013000', NOW(), NOW()),
(1117, '도우미1117', 'FEMALE', 'HELPER', '1996-11-05', 37.4672, 127.0656, 'https://example.com/profiles/dummy.jpg', '서울 강남구 일원동', '1168011400', NOW(), NOW()),
(1118, '도우미1118', 'MALE', 'HELPER', '1990-07-20', 37.5452, 127.0036, 'https://example.com/profiles/dummy.jpg', '서울 용산구 한남동', '1117013100', NOW(), NOW()),
(1119, '도우미1119', 'FEMALE', 'HELPER', '1994-02-10', 37.4572, 127.0756, 'https://example.com/profiles/dummy.jpg', '서울 송파구 가락동', '1171010700', NOW(), NOW()),
(1120, '도우미1120', 'MALE', 'HELPER', '1991-09-15', 37.5032, 127.0416, 'https://example.com/profiles/dummy.jpg', '서울 강남구 역삼동', '1168010100', NOW(), NOW()),
(1121, '도우미1121', 'FEMALE', 'HELPER', '1995-04-25', 37.4992, 127.0376, 'https://example.com/profiles/dummy.jpg', '서울 강남구 역삼동', '1168010100', NOW(), NOW()),
(1122, '도우미1122', 'MALE', 'HELPER', '1988-12-05', 37.5182, 127.0316, 'https://example.com/profiles/dummy.jpg', '서울 강남구 논현동', '1168010800', NOW(), NOW()),
(1123, '도우미1123', 'FEMALE', 'HELPER', '1993-06-18', 37.4842, 127.0476, 'https://example.com/profiles/dummy.jpg', '서울 강남구 도곡동', '1168011800', NOW(), NOW()),
(1124, '도우미1124', 'MALE', 'HELPER', '1990-01-30', 37.5282, 127.0216, 'https://example.com/profiles/dummy.jpg', '서울 강남구 신사동', '1168010700', NOW(), NOW()),
(1125, '도우미1125', 'FEMALE', 'HELPER', '1994-08-08', 37.4742, 127.0576, 'https://example.com/profiles/dummy.jpg', '서울 강남구 개포동', '1168010300', NOW(), NOW()),
(1126, '도우미1126', 'MALE', 'HELPER', '1987-03-22', 37.5382, 127.0116, 'https://example.com/profiles/dummy.jpg', '서울 용산구 한남동', '1117013100', NOW(), NOW()),
(1127, '도우미1127', 'FEMALE', 'HELPER', '1996-10-12', 37.4642, 127.0676, 'https://example.com/profiles/dummy.jpg', '서울 강남구 수서동', '1168011500', NOW(), NOW()),
(1128, '도우미1128', 'MALE', 'HELPER', '1991-05-28', 37.5482, 127.0016, 'https://example.com/profiles/dummy.jpg', '서울 용산구 이태원동', '1117013000', NOW(), NOW()),
(1129, '도우미1129', 'FEMALE', 'HELPER', '1993-07-14', 37.4542, 127.0776, 'https://example.com/profiles/dummy.jpg', '서울 송파구 문정동', '1171010800', NOW(), NOW()),
(1130, '도우미1130', 'MALE', 'HELPER', '1989-11-30', 37.5022, 127.0406, 'https://example.com/profiles/dummy.jpg', '서울 강남구 역삼동', '1168010100', NOW(), NOW()),
(1131, '도우미1131', 'FEMALE', 'HELPER', '1995-02-18', 37.5002, 127.0386, 'https://example.com/profiles/dummy.jpg', '서울 강남구 역삼동', '1168010100', NOW(), NOW()),
(1132, '도우미1132', 'MALE', 'HELPER', '1991-09-24', 37.5192, 127.0306, 'https://example.com/profiles/dummy.jpg', '서울 강남구 논현동', '1168010800', NOW(), NOW()),
(1133, '도우미1133', 'FEMALE', 'HELPER', '1994-04-09', 37.4832, 127.0486, 'https://example.com/profiles/dummy.jpg', '서울 강남구 도곡동', '1168011800', NOW(), NOW()),
(1134, '도우미1134', 'MALE', 'HELPER', '1988-12-01', 37.5292, 127.0206, 'https://example.com/profiles/dummy.jpg', '서울 강남구 신사동', '1168010700', NOW(), NOW()),
(1135, '도우미1135', 'FEMALE', 'HELPER', '1993-06-15', 37.4732, 127.0586, 'https://example.com/profiles/dummy.jpg', '서울 강남구 개포동', '1168010300', NOW(), NOW()),
(1136, '도우미1136', 'MALE', 'HELPER', '1991-01-11', 37.5392, 127.0106, 'https://example.com/profiles/dummy.jpg', '서울 용산구 한남동', '1117013100', NOW(), NOW()),
(1137, '도우미1137', 'FEMALE', 'HELPER', '1995-08-27', 37.4632, 127.0686, 'https://example.com/profiles/dummy.jpg', '서울 강남구 수서동', '1168011500', NOW(), NOW()),
(1138, '도우미1138', 'MALE', 'HELPER', '1987-03-03', 37.5492, 127.0006, 'https://example.com/profiles/dummy.jpg', '서울 용산구 이태원동', '1117013000', NOW(), NOW()),
(1139, '도우미1139', 'FEMALE', 'HELPER', '1996-10-19', 37.4532, 127.0786, 'https://example.com/profiles/dummy.jpg', '서울 송파구 문정동', '1171010800', NOW(), NOW()),
(1140, '도우미1140', 'MALE', 'HELPER', '1992-05-22', 37.5080, 127.0480, 'https://example.com/profiles/dummy.jpg', '서울 강남구 삼성동', '1168010500', NOW(), NOW()),
(1141, '도우미1141', 'FEMALE', 'HELPER', '1994-09-08', 37.4950, 127.0310, 'https://example.com/profiles/dummy.jpg', '서울 강남구 역삼동', '1168010100', NOW(), NOW()),
(1142, '도우미1142', 'MALE', 'HELPER', '1989-11-14', 37.5230, 127.0270, 'https://example.com/profiles/dummy.jpg', '서울 강남구 논현동', '1168010800', NOW(), NOW()),
(1143, '도우미1143', 'FEMALE', 'HELPER', '1995-02-26', 37.4790, 127.0520, 'https://example.com/profiles/dummy.jpg', '서울 강남구 개포동', '1168010300', NOW(), NOW()),
(1144, '도우미1144', 'MALE', 'HELPER', '1990-07-12', 37.5330, 127.0170, 'https://example.com/profiles/dummy.jpg', '서울 서초구 잠원동', '1165010600', NOW(), NOW()),
(1145, '도우미1145', 'FEMALE', 'HELPER', '1993-12-29', 37.4690, 127.0620, 'https://example.com/profiles/dummy.jpg', '서울 강남구 일원동', '1168011400', NOW(), NOW()),
(1146, '도우미1146', 'MALE', 'HELPER', '1988-04-16', 37.5430, 127.0060, 'https://example.com/profiles/dummy.jpg', '서울 용산구 한남동', '1117013100', NOW(), NOW()),
(1147, '도우미1147', 'FEMALE', 'HELPER', '1996-08-02', 37.4590, 127.0720, 'https://example.com/profiles/dummy.jpg', '서울 송파구 가락동', '1171010700', NOW(), NOW()),
(1148, '도우미1148', 'MALE', 'HELPER', '1991-01-24', 37.5530, 126.9970, 'https://example.com/profiles/dummy.jpg', '서울 중구 필동', '1114011400', NOW(), NOW()),
(1149, '도우미1149', 'FEMALE', 'HELPER', '1994-06-11', 37.4490, 127.0820, 'https://example.com/profiles/dummy.jpg', '서울 송파구 장지동', '1171010900', NOW(), NOW())
ON DUPLICATE KEY UPDATE
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
    
    -- ========================================
    -- 도우미 회원-도움 유형 매핑 (회원당 1~3개 랜덤 할당)
    -- ========================================
    INSERT INTO member_help_category_sync (member_id, help_category_id, created_at, updated_at)
    SELECT T.member_id, T.cat_id, NOW(), NOW()
    FROM (
        -- 1. 기본: 모든 회원에게 1개 할당 (ID % 8 + 1)
        SELECT member_id, (member_id % 8) + 1 AS cat_id
        FROM member_sync
        WHERE member_id BETWEEN 1100 AND 1149
    
        UNION
    
        -- 2. 짝수 ID 회원에게 추가 1개 할당 ((ID + 3) % 8 + 1)
        SELECT member_id, ((member_id + 3) % 8) + 1 AS cat_id
        FROM member_sync
        WHERE member_id BETWEEN 1100 AND 1149
        AND member_id % 2 = 0
    
        UNION
    
        -- 3. 3의 배수 ID 회원에게 추가 1개 할당 ((ID + 5) % 8 + 1)
        SELECT member_id, ((member_id + 5) % 8) + 1 AS cat_id
        FROM member_sync
        WHERE member_id BETWEEN 1100 AND 1149
        AND member_id % 3 = 0
    ) T
    ON DUPLICATE KEY UPDATE updated_at = NOW();
    