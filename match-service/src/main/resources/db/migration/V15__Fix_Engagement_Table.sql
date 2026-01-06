-- 외래키 체크 비활성화 (TRUNCATE를 위해 필요)
SET FOREIGN_KEY_CHECKS = 0;


-- 1) 기존 FK 제약조건 삭제
ALTER TABLE `engagement`
  DROP FOREIGN KEY `FK_engagement_TO_agreement`;

-- 2) agreement_id 컬럼 삭제
ALTER TABLE `engagement`
  DROP COLUMN `agreement_id`;

-- 3) match_id 컬럼 추가 (원하는 위치에 넣고 싶으면 AFTER 사용)
ALTER TABLE `engagement`
  ADD COLUMN `match_id` BIGINT NOT NULL AFTER `engagement_id`;

-- 5) match_id로 FK 추가 (참조 테이블/컬럼명은 실제 스키마에 맞게 수정)
-- 예: match 테이블의 match_id를 참조하는 경우
ALTER TABLE `engagement`
  ADD CONSTRAINT `FK_engagement_TO_match`
  FOREIGN KEY (`match_id`) REFERENCES `match` (`match_id`);


-- 외래키 체크 재활성화
SET FOREIGN_KEY_CHECKS = 1;