-- =====================================================
-- Member Service 더미 데이터 - HoneyWallet Sync (회원 100, 700)
-- =====================================================
-- Payment Service의 HoneyWallet 데이터와 싱크
--
-- [회원 정보]
-- memberId 100: 김철수 (장애인, 60대 남성) - 5,000,000원 (50,000꿀)
-- memberId 700: 강지훈 (도우미, 30대 남성) - 500,000원 (5,000꿀)
-- =====================================================

-- 회원 100 (김철수, 장애인): 잔액 5,000,000원
-- Payment Service의 honey_wallet_id=100, balance=5000000과 동일
INSERT INTO member_honey_wallet_sync (honey_wallet_id, member_id, balance, created_at, updated_at) VALUES
(100, 100, 5000000, DATE_SUB(NOW(), INTERVAL 60 DAY), NOW());

-- 회원 700 (강지훈, 도우미): 잔액 500,000원
-- Payment Service의 honey_wallet_id=700, balance=500000과 동일
INSERT INTO member_honey_wallet_sync (honey_wallet_id, member_id, balance, created_at, updated_at) VALUES
(700, 700, 500000, DATE_SUB(NOW(), INTERVAL 50 DAY), NOW());