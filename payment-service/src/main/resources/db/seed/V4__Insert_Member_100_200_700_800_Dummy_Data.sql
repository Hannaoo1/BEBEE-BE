-- =====================================================
-- Payment Service 더미 데이터 (Member 100, 200, 700, 800)
-- =====================================================
-- Member 서비스와 Match 서비스 더미 데이터 싱크
--
-- [회원 정보]
-- memberId 100: 김철수 (장애인, 60대 남성)
-- memberId 200: 김민수 (장애인, 70대 남성)
-- memberId 700: 강지훈 (도우미, 30대 남성)
-- memberId 800: 윤서연 (도우미, 30대 여성)
--
-- [Agreements - 모두 BEFORE 상태]
-- 10002: helper 800, disabled 100, TERM, 40,000꿀 필요 (장애인 100은 50,000꿀 보유 → 충분)
-- 10006: helper 800, disabled 200, DAY, 10,000꿀 필요 (장애인 200은 5,000꿀 보유 → 부족!)
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- 1. HoneyWallet: 회원 100, 200, 700, 800
-- =====================================================

-- 장애인 100 (김철수): 잔액 5,000,000원 (50,000꿀)
-- 시나리오: 초기 5,000,000원 충전 → Agreement 10002 아직 BEFORE → 에스크로 사용 전
INSERT INTO honey_wallet (honey_wallet_id, member_id, balance, created_at, updated_at) VALUES
(100, 100, 5000000, DATE_SUB(NOW(), INTERVAL 60 DAY), NOW());

-- 장애인 200 (김민수): 잔액 500,000원 (5,000꿀)
-- 시나리오: 초기 500,000원 충전 → Agreement 10006은 10,000꿀 필요 → 부족!
INSERT INTO honey_wallet (honey_wallet_id, member_id, balance, created_at, updated_at) VALUES
(200, 200, 500000, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW());

-- 도우미 700 (강지훈): 잔액 500,000원 (5,000꿀)
-- 시나리오: 과거 활동 보상 500,000원 획득
INSERT INTO honey_wallet (honey_wallet_id, member_id, balance, created_at, updated_at) VALUES
(700, 700, 500000, DATE_SUB(NOW(), INTERVAL 50 DAY), NOW());

-- 도우미 800 (윤서연): 잔액 0원
-- 시나리오: 신규 도우미, 아직 활동 없음
INSERT INTO honey_wallet (honey_wallet_id, member_id, balance, created_at, updated_at) VALUES
(800, 800, 0, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW());

-- =====================================================
-- 2. Payment: 결제 내역
-- =====================================================

-- 장애인 100 - 초기 충전 5,000,000원 (PAID)
INSERT INTO payment (payment_id, member_id, payment_key, order_id, amount, status, created_at, updated_at) VALUES
(100001, 100, 'test_payment_key_100_001', 'ORDER_100_20250101', 5000000, 'PAID', DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 60 DAY));

-- 장애인 200 - 초기 충전 500,000원 (PAID)
INSERT INTO payment (payment_id, member_id, payment_key, order_id, amount, status, created_at, updated_at) VALUES
(200001, 200, 'test_payment_key_200_001', 'ORDER_200_20250102', 500000, 'PAID', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY));

-- =====================================================
-- 3. HoneyHistory: 거래 내역
-- =====================================================

-- === 장애인 100 (김철수) 거래 내역 ===

-- 장애인 100 - 초기 충전 5,000,000원
INSERT INTO honey_history (honey_history_id, honey_wallet_id, target_member_id, amount, type, created_at, updated_at) VALUES
(1000, 100, 100, 5000000, 'CHARGE', DATE_SUB(NOW(), INTERVAL 60 DAY), DATE_SUB(NOW(), INTERVAL 60 DAY));

-- === 장애인 200 (김민수) 거래 내역 ===

-- 장애인 200 - 초기 충전 500,000원
INSERT INTO honey_history (honey_history_id, honey_wallet_id, target_member_id, amount, type, created_at, updated_at) VALUES
(2000, 200, 200, 500000, 'CHARGE', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY));

-- === 도우미 700 (강지훈) 거래 내역 ===

-- 도우미 700 - 과거 활동 보상 500,000원
INSERT INTO honey_history (honey_history_id, honey_wallet_id, target_member_id, amount, type, created_at, updated_at) VALUES
(7000, 700, 700, 500000, 'EARNING', DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 50 DAY));

-- === 도우미 800 (윤서연) 거래 내역 ===
-- 없음 (신규 도우미, 아직 활동 없음)

-- =====================================================
-- 4. PaymentAgreementSync: Agreement 싱크
-- =====================================================
-- Match 서비스의 Agreement와 싱크 (모두 BEFORE 상태)
-- 주의: unit_honey, total_honey는 꿀 단위

-- Agreement 10002: TERM, 40,000꿀 (4,000,000원), BEFORE
-- disabled 100 (50,000꿀 보유), helper 800
-- 잔액 충분, 아직 에스크로 사용 전
INSERT INTO payment_agreement_sync (agreement_id, unit_honey, total_honey, type, created_at, updated_at) VALUES
(10002, 4000, 40000, 'TERM', DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY));

-- Agreement 10006: DAY, 10,000꿀 (1,000,000원), BEFORE
-- disabled 200 (5,000꿀 보유), helper 800
-- 장애인 200은 5,000꿀만 보유 → 부족!
INSERT INTO payment_agreement_sync (agreement_id, unit_honey, total_honey, type, created_at, updated_at) VALUES
(10006, 10000, 10000, 'DAY', DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY));

-- =====================================================
-- 5. PaymentMatchSync: Match 싱크
-- =====================================================
-- BEFORE 상태이므로 아직 Match 없음

-- =====================================================
-- 6. HoneyEscrow: 에스크로
-- =====================================================
-- BEFORE 상태이므로 아직 Escrow 없음

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 더미 데이터 요약
-- =====================================================
-- HoneyWallet: 4개
--   - 장애인 100 (김철수): 5,000,000원 (50,000꿀) ✅ Agreement 10002 필요 40,000꿀 → 충분!
--   - 장애인 200 (김민수): 500,000원 (5,000꿀) ⚠️ Agreement 10006 필요 10,000꿀 → 부족!
--   - 도우미 700 (강지훈): 500,000원 (과거 활동 보상)
--   - 도우미 800 (윤서연): 0원 (신규)
--
-- Payment: 2개
--   - 장애인 100 초기 충전 5,000,000원 (PAID)
--   - 장애인 200 초기 충전 500,000원 (PAID)
--
-- HoneyHistory: 4개
--   - 장애인 100: 충전 5,000,000원
--   - 장애인 200: 충전 500,000원
--   - 도우미 700: 획득 500,000원
--   - 도우미 800: 없음
--
-- PaymentAgreementSync: 2개
--   - 10002: TERM, 40,000꿀, BEFORE (장애인 100 잔액 충분)
--   - 10006: DAY, 10,000꿀, BEFORE (장애인 200 잔액 부족)
--
-- PaymentMatchSync: 없음 (BEFORE 상태)
-- HoneyEscrow: 없음 (BEFORE 상태)
--
-- =====================================================
-- 테스트 시나리오
-- =====================================================
-- 1. 잔액 충분 케이스: 장애인 100, Agreement 10002
--    - 보유: 50,000꿀 (5,000,000원)
--    - 필요: 40,000꿀 (4,000,000원)
--    - 상태: BEFORE (에스크로 사용 전)
--    - 결과: ✅ 에스크로 생성 가능
--
-- 2. 잔액 부족 케이스: 장애인 200, Agreement 10006
--    - 보유: 5,000꿀 (500,000원)
--    - 필요: 10,000꿀 (1,000,000원)
--    - 상태: BEFORE
--    - 결과: ⚠️ 에스크로 생성 불가 (추가 충전 필요)
--
-- 3. 신규 도우미: 도우미 800
--    - 잔액: 0원
--    - 히스토리: 없음
--    - Agreement 2개에 배정 (10002, 10006)
-- =====================================================
