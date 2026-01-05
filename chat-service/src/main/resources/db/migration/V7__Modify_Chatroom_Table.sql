-- ========================================
-- V7: Chatroom 테이블 수정
-- ========================================
-- 1. 유니크 제약 조건 (uk_chatroom_members) 제거
-- 2. match_status 필드 추가

-- Step 1: 외래 키 제약 조건 삭제
ALTER TABLE chatroom
DROP FOREIGN KEY fk_chatroom_member1;

ALTER TABLE chatroom
DROP FOREIGN KEY fk_chatroom_member2;

-- Step 2: 유니크 인덱스 삭제
ALTER TABLE chatroom
DROP INDEX uk_chatroom_members;

-- Step 3: member1_id, member2_id에 대한 개별 인덱스 생성 (외래 키를 위해)
ALTER TABLE chatroom
ADD INDEX idx_chatroom_member1 (member1_id);

ALTER TABLE chatroom
ADD INDEX idx_chatroom_member2 (member2_id);

-- Step 4: match_status 컬럼 추가
ALTER TABLE chatroom
ADD COLUMN match_status ENUM('NON_MATCHED','PROCEEDING','MATCHED') NOT NULL DEFAULT 'NON_MATCHED' COMMENT '매칭 상태';

-- Step 5: 외래 키 제약 조건 재생성
ALTER TABLE chatroom
ADD CONSTRAINT fk_chatroom_member1
    FOREIGN KEY (member1_id) REFERENCES member_sync (member_id);

ALTER TABLE chatroom
ADD CONSTRAINT fk_chatroom_member2
    FOREIGN KEY (member2_id) REFERENCES member_sync (member_id);