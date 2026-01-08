-- =====================================================
-- V5: 기존 더미 데이터 삭제 (Member 1-4)
-- =====================================================
-- Member 서비스에 존재하지 않는 임시 테스트 데이터 삭제
-- Member 1, 2, 3, 4는 V3에서 생성되었으나 실제 member 서비스에 없음
-- Member 100, 200, 700, 800만 유지 (V4에서 추가)
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- 1. HoneyEscrow 삭제 (FK 제약 조건 때문에 먼저 삭제)
-- =====================================================
DELETE FROM honey_escrow WHERE escrow_id IN (1, 2);

-- =====================================================
-- 2. PaymentMatchSync 삭제
-- =====================================================
DELETE FROM payment_match_sync WHERE match_id IN (1, 2, 3);

-- =====================================================
-- 3. PaymentAgreementSync 삭제
-- =====================================================
DELETE FROM payment_agreement_sync WHERE agreement_id IN (1, 2, 3);

-- =====================================================
-- 4. HoneyHistory 삭제
-- =====================================================
DELETE FROM honey_history WHERE honey_wallet_id IN (1, 2, 3, 4);

-- =====================================================
-- 5. Payment 삭제
-- =====================================================
DELETE FROM payment WHERE payment_id IN (1, 2, 3);

-- =====================================================
-- 6. HoneyWallet 삭제
-- =====================================================
DELETE FROM honey_wallet WHERE honey_wallet_id IN (1, 2, 3, 4);

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 삭제 완료 요약
-- =====================================================
-- 삭제된 데이터:
--   - HoneyWallet: member_id 1, 2, 3, 4 (4개)
--   - Payment: payment_id 1, 2, 3 (3개)
--   - HoneyHistory: honey_wallet_id 1, 2, 3, 4 관련 (7개)
--   - PaymentAgreementSync: agreement_id 1, 2, 3 (3개)
--   - PaymentMatchSync: match_id 1, 2, 3 (3개)
--   - HoneyEscrow: escrow_id 1, 2 (2개)
--
-- 남은 데이터 (Member 서비스와 싱크):
--   - HoneyWallet: member_id 100, 200, 700, 800
--   - Payment: payment_id 100001, 200001
--   - HoneyHistory: member 100, 200, 700 관련
--   - PaymentAgreementSync: agreement_id 10002, 10006
--   - PaymentMatchSync: 없음 (BEFORE 상태)
--   - HoneyEscrow: 없음 (BEFORE 상태)
-- =====================================================
