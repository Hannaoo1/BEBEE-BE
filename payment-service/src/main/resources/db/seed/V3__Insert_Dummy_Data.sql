-- =====================================================
-- Payment Service 더미 데이터
-- =====================================================
-- 회원 4명 (장애인 2명, 도우미 2명)
-- 장애인 1: memberId = 1 (잔액: 50,000원 = 500꿀)
-- 장애인 2: memberId = 2 (잔액: 100,000원 = 1,000꿀)
-- 도우미 1: memberId = 3 (잔액: 30,000원 = 300꿀)
-- 도우미 2: memberId = 4 (잔액: 20,000원 = 200꿀)
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;

-- 1. HoneyWallet 테이블 데이터
-- 장애인 회원 2명
INSERT INTO honey_wallet (honey_wallet_id, member_id, balance, created_at, updated_at) VALUES
(1, 1, 50000, NOW(), NOW()),  -- 장애인 1: 50,000원
(2, 2, 100000, NOW(), NOW()); -- 장애인 2: 100,000원

-- 도우미 회원 2명
INSERT INTO honey_wallet (honey_wallet_id, member_id, balance, created_at, updated_at) VALUES
(3, 3, 30000, NOW(), NOW()),  -- 도우미 1: 30,000원
(4, 4, 20000, NOW(), NOW());  -- 도우미 2: 20,000원

-- 2. HoneyHistory 테이블 데이터 (초기 충전 내역)
-- 장애인 1 - 초기 충전 50,000원
INSERT INTO honey_history (honey_history_id, honey_wallet_id, target_member_id, amount, type, created_at, updated_at) VALUES
(1, 1, 1, 50000, 'CHARGE', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY));

-- 장애인 2 - 초기 충전 100,000원
INSERT INTO honey_history (honey_history_id, honey_wallet_id, target_member_id, amount, type, created_at, updated_at) VALUES
(2, 2, 2, 100000, 'CHARGE', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY));

-- 도우미 1 - 활동 보상 30,000원
INSERT INTO honey_history (honey_history_id, honey_wallet_id, target_member_id, amount, type, created_at, updated_at) VALUES
(3, 3, 3, 30000, 'EARNING', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY));

-- 도우미 2 - 활동 보상 20,000원
INSERT INTO honey_history (honey_history_id, honey_wallet_id, target_member_id, amount, type, created_at, updated_at) VALUES
(4, 4, 4, 20000, 'EARNING', DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY));

-- 3. Payment 테이블 데이터 (결제 내역)
-- 장애인 1의 결제 내역 - PAID (성공)
INSERT INTO payment (payment_id, member_id, payment_key, order_id, amount, status, created_at, updated_at) VALUES
(1, 1, 'test_payment_key_001', 'ORDER_001_20250107', 50000, 'PAID', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY));

-- 장애인 2의 결제 내역 - PAID (성공)
INSERT INTO payment (payment_id, member_id, payment_key, order_id, amount, status, created_at, updated_at) VALUES
(2, 2, 'test_payment_key_002', 'ORDER_002_20250107', 100000, 'PAID', DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 25 DAY));

-- 장애인 1의 추가 결제 - CANCELED (취소)
INSERT INTO payment (payment_id, member_id, payment_key, order_id, amount, status, created_at, updated_at) VALUES
(3, 1, 'test_payment_key_003', 'ORDER_003_20250107', 10000, 'CANCELED', DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY));

-- 4. PaymentAgreementSync 테이블 데이터 (협약 정보)
-- 협약 1: 일일 단가 100꿀
INSERT INTO payment_agreement_sync (agreement_id, unit_honey, total_honey, type, created_at, updated_at) VALUES
(1, 100, 1000, 'DAY', DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY));

-- 협약 2: 기간제 총 500꿀
INSERT INTO payment_agreement_sync (agreement_id, unit_honey, total_honey, type, created_at, updated_at) VALUES
(2, 50, 500, 'TERM', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY));

-- 협약 3: 일일 단가 150꿀
INSERT INTO payment_agreement_sync (agreement_id, unit_honey, total_honey, type, created_at, updated_at) VALUES
(3, 150, 1500, 'DAY', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 5. PaymentMatchSync 테이블 데이터 (매칭 정보)
-- 매칭 1: 장애인 1 + 도우미 1, 협약 1
INSERT INTO payment_match_sync (match_id, helper_id, disabled_id, agreement_id, created_at, updated_at) VALUES
(1, 3, 1, 1, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY));

-- 매칭 2: 장애인 2 + 도우미 2, 협약 2
INSERT INTO payment_match_sync (match_id, helper_id, disabled_id, agreement_id, created_at, updated_at) VALUES
(2, 4, 2, 2, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY));

-- 매칭 3: 장애인 1 + 도우미 2, 협약 3 (새로운 매칭)
INSERT INTO payment_match_sync (match_id, helper_id, disabled_id, agreement_id, created_at, updated_at) VALUES
(3, 4, 1, 3, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 6. HoneyEscrow 테이블 데이터 (에스크로 정보)
-- 에스크로 1: 매칭 1 - COMPLETED (도우미에게 전달 완료)
INSERT INTO honey_escrow (escrow_id, match_id, disabled_id, helper_id, amount, status, completed_at, cancelled_at, created_at, updated_at) VALUES
(1, 1, 1, 3, 10000, 'COMPLETED', DATE_SUB(NOW(), INTERVAL 6 DAY), NULL, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY));

-- 에스크로 2: 매칭 2 - PENDING (임시 보관 중)
INSERT INTO honey_escrow (escrow_id, match_id, disabled_id, helper_id, amount, status, completed_at, cancelled_at, created_at, updated_at) VALUES
(2, 2, 2, 4, 5000, 'PENDING', NULL, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY));

-- 7. 에스크로 관련 HoneyHistory 데이터
-- 장애인 1 - 에스크로 차감 (매칭 1)
INSERT INTO honey_history (honey_history_id, honey_wallet_id, target_member_id, amount, type, created_at, updated_at) VALUES
(5, 1, 1, 10000, 'ESCROWED', DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY));

-- 도우미 1 - 에스크로에서 획득 (매칭 1 완료)
INSERT INTO honey_history (honey_history_id, honey_wallet_id, target_member_id, amount, type, created_at, updated_at) VALUES
(6, 3, 3, 10000, 'EARNING', DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY));

-- 장애인 2 - 에스크로 차감 (매칭 2, 아직 PENDING)
INSERT INTO honey_history (honey_history_id, honey_wallet_id, target_member_id, amount, type, created_at, updated_at) VALUES
(7, 2, 2, 5000, 'ESCROWED', DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY));

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 더미 데이터 요약
-- =====================================================
-- HoneyWallet: 4개 (회원 4명)
--   - 장애인 1 (ID=1): 50,000원 (에스크로 차감 후 실제: 40,000원)
--   - 장애인 2 (ID=2): 100,000원 (에스크로 차감 후 실제: 95,000원)
--   - 도우미 1 (ID=3): 30,000원 (에스크로 획득 포함)
--   - 도우미 2 (ID=4): 20,000원
--
-- HoneyHistory: 7개
--   - 초기 충전/보상: 4개
--   - 에스크로 관련: 3개
--
-- Payment: 3개
--   - PAID: 2개 (장애인 1, 2의 초기 충전)
--   - CANCELED: 1개 (장애인 1의 취소된 결제)
--
-- PaymentAgreementSync: 3개
-- PaymentMatchSync: 3개
-- HoneyEscrow: 2개
--   - COMPLETED: 1개 (매칭 1)
--   - PENDING: 1개 (매칭 2)
-- =====================================================