-- FK 제약조건의 테이블 이름 대소문자 오류 수정 (Honey_wallet -> honey_wallet)

-- 1. 기존 FK 제약조건 삭제
ALTER TABLE honey_history
DROP FOREIGN KEY FK_history_TO_wallet;

-- 2. 올바른 테이블 이름으로 FK 제약조건 재생성
ALTER TABLE honey_history
ADD CONSTRAINT FK_history_TO_wallet
    FOREIGN KEY (honey_wallet_id)
    REFERENCES honey_wallet (honey_wallet_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;