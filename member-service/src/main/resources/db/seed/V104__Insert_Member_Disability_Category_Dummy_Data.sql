-- ========================================
-- V14: Member Disability Category 더미 데이터
-- ========================================
-- 장애인 회원의 장애 유형 매핑
--
-- 장애 유형:
-- 1: 지체장애, 2: 시각장애, 3: 청각장애
-- 4: 발달장애, 5: 내부기관장애, 6: 기타장애
--
-- 장애인 회원:
-- 100: 김철수, 200: 김민수, 300: 이영희
-- 400: 박민수, 500: 최은정, 600: 정수진

INSERT INTO member_disability_category (member_id, disability_category_id, level, disability_description, created_at, updated_at)
VALUES
-- 장애인 100 (김철수): 지체장애 + 시각장애
(100, 1, '2', '하지 마비로 인한 보행 장애', NOW(), NOW()),
(100, 2, '3', '양안 시력 저하', NOW(), NOW()),

-- 장애인 200 (김민수): 청각장애
(200, 3, '2', '양측 난청으로 인한 의사소통 장애', NOW(), NOW()),

-- 장애인 300 (이영희): 발달장애 + 지체장애
(300, 4, '1', '지적장애로 인한 일상생활 지원 필요', NOW(), NOW()),
(300, 1, '3', '상지 기능 저하', NOW(), NOW()),

-- 장애인 400 (박민수): 내부기관장애
(400, 5, '2', '신장 기능 장애로 정기 투석 필요', NOW(), NOW()),

-- 장애인 500 (최은정): 시각장애 + 청각장애
(500, 2, '1', '전맹으로 인한 일상생활 지원 필요', NOW(), NOW()),
(500, 3, '3', '경도 난청', NOW(), NOW()),

-- 장애인 600 (정수진): 기타장애
(600, 6, '2', '희귀질환으로 인한 이동 지원 필요', NOW(), NOW())

ON DUPLICATE KEY UPDATE
    level = VALUES(level),
    disability_description = VALUES(disability_description),
    updated_at = NOW();
