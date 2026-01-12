-- ========================================
-- Engagement 더미 데이터 재구성
-- ========================================
-- 기존 데이터 삭제 후 재삽입
-- 80%는 과거(ACTIVE), 20%는 미래(INACTIVE)
-- 2025년 12월 ~ 2026년 1월 데이터
-- 모두 is_disabled_check=FALSE, is_helper_check=FALSE
-- ========================================

-- 기존 engagement 전체 삭제
DELETE FROM engagement;

-- ========================================
-- 하루도움(DAY) Engagements
-- ========================================

-- DAY 1: 2025-12-20 (금) - 과거, ACTIVE
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73001, 40006, '2025-12-20', 'DAY', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- DAY 2: 2025-12-28 (토) - 과거, ACTIVE
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73002, 40009, '2025-12-28', 'DAY', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- DAY 3: 2026-01-03 (금) - 과거, ACTIVE
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73003, 40007, '2026-01-03', 'DAY', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- DAY 4: 2026-01-06 (화) - 과거, ACTIVE
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73004, 40009, '2026-01-06', 'DAY', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- DAY 5: 2026-01-08 (수) - 과거, ACTIVE
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73005, 40008, '2026-01-08', 'DAY', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- DAY 6: 2026-01-09 (목) - 과거, ACTIVE
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73006, 40006, '2026-01-09', 'DAY', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- DAY 7: 2026-01-10 (금) - 과거, ACTIVE
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73007, 40007, '2026-01-10', 'DAY', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- DAY 8: 2026-01-11 (토) - 과거, ACTIVE
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73008, 40008, '2026-01-11', 'DAY', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- DAY 9: 2026-01-17 (금) - 미래, INACTIVE
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73009, 40007, '2026-01-17', 'DAY', 'INACTIVE', FALSE, FALSE, NOW(), NOW());

-- DAY 10: 2026-01-23 (목) - 미래, INACTIVE
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES (73010, 40008, '2026-01-23', 'DAY', 'INACTIVE', FALSE, FALSE, NOW(), NOW());

-- ========================================
-- 지속도움(TERM) Engagements - Match 40002
-- 문화센터 수업 동행 (월, 수)
-- ========================================

-- 2025년 12월
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(70001, 40002, '2025-12-16', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(70002, 40002, '2025-12-18', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(70003, 40002, '2025-12-23', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(70004, 40002, '2025-12-25', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(70005, 40002, '2025-12-30', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- 2026년 1월
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(70006, 40002, '2026-01-06', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(70007, 40002, '2026-01-08', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(70008, 40002, '2026-01-13', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW()),
(70009, 40002, '2026-01-15', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW()),
(70010, 40002, '2026-01-20', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW());

-- ========================================
-- 지속도움(TERM) Engagements - Match 40003
-- 정기 통원 치료 동행 (화, 목)
-- ========================================

-- 2025년 12월
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(71001, 40003, '2025-12-17', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(71002, 40003, '2025-12-19', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(71003, 40003, '2025-12-24', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(71004, 40003, '2025-12-26', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(71005, 40003, '2025-12-31', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- 2026년 1월
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(71006, 40003, '2026-01-02', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(71007, 40003, '2026-01-07', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(71008, 40003, '2026-01-09', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(71009, 40003, '2026-01-14', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW()),
(71010, 40003, '2026-01-16', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW());

-- ========================================
-- 지속도움(TERM) Engagements - Match 40004
-- 주 2회 방문 목욕 서비스 (화, 목)
-- ========================================

-- 2025년 12월
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(72001, 40004, '2025-12-17', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(72002, 40004, '2025-12-19', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(72003, 40004, '2025-12-24', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(72004, 40004, '2025-12-26', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(72005, 40004, '2025-12-31', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- 2026년 1월
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(72006, 40004, '2026-01-02', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(72007, 40004, '2026-01-07', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(72008, 40004, '2026-01-09', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(72009, 40004, '2026-01-14', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW()),
(72010, 40004, '2026-01-16', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW());

-- ========================================
-- 지속도움(TERM) Engagements - Match 40005
-- 월~금 정기 활동 (V21에서 동적 생성되는 것 대체)
-- ========================================

-- 2025년 12월
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(60001, 40005, '2025-12-16', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60002, 40005, '2025-12-17', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60003, 40005, '2025-12-18', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60004, 40005, '2025-12-19', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60005, 40005, '2025-12-20', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60006, 40005, '2025-12-23', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60007, 40005, '2025-12-24', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60008, 40005, '2025-12-26', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60009, 40005, '2025-12-27', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60010, 40005, '2025-12-30', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60011, 40005, '2025-12-31', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW());

-- 2026년 1월
INSERT INTO engagement (engagement_id, match_id, date, type, status, is_disabled_check, is_helper_check, created_at, updated_at)
VALUES
(60012, 40005, '2026-01-02', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60013, 40005, '2026-01-03', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60014, 40005, '2026-01-06', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60015, 40005, '2026-01-07', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60016, 40005, '2026-01-08', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60017, 40005, '2026-01-09', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60018, 40005, '2026-01-10', 'TERM', 'ACTIVE', FALSE, FALSE, NOW(), NOW()),
(60019, 40005, '2026-01-13', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW()),
(60020, 40005, '2026-01-14', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW()),
(60021, 40005, '2026-01-15', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW()),
(60022, 40005, '2026-01-16', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW()),
(60023, 40005, '2026-01-17', 'TERM', 'INACTIVE', FALSE, FALSE, NOW(), NOW());

-- ========================================
-- 요약
-- ========================================
-- 하루도움(DAY): 10개 (8개 ACTIVE, 2개 INACTIVE = 80%)
-- 지속도움(TERM):
--   - Match 40002: 10개 (7개 ACTIVE, 3개 INACTIVE = 70%)
--   - Match 40003: 10개 (8개 ACTIVE, 2개 INACTIVE = 80%)
--   - Match 40004: 10개 (8개 ACTIVE, 2개 INACTIVE = 80%)
--   - Match 40005: 22개 (18개 ACTIVE, 4개 INACTIVE = 82%)
-- 전체: 62개 (49개 ACTIVE, 13개 INACTIVE = 79% ACTIVE)
-- ========================================