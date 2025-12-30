-- ========================================
-- V8: member_sync 테이블 스키마 수정
-- ========================================
-- MemberSync 엔티티와 테이블 스키마를 일치시키기 위한 마이그레이션

-- 외래키 체크 비활성화 (TRUNCATE를 위해 필요)
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 기존 데이터 삭제 (birthDate가 없어서 새로 넣어야 함)
TRUNCATE TABLE member_sync;

-- 2. sweetness 컬럼 삭제 (엔티티에 없음)
ALTER TABLE member_sync DROP COLUMN sweetness;

-- 3. birth_date 컬럼 추가 (엔티티에 있음, ageGroup 계산에 필요)
ALTER TABLE member_sync ADD COLUMN birth_date DATE NOT NULL;

-- 4. address_road 컬럼 추가 (엔티티에 있음)
ALTER TABLE member_sync ADD COLUMN address_road VARCHAR(255) NULL;

-- 외래키 체크 재활성화
SET FOREIGN_KEY_CHECKS = 1;