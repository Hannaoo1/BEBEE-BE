-- =============================================
-- Outbox 테이블 생성
-- Transactional Outbox 패턴을 위한 이벤트 발행 테이블
-- =============================================

CREATE TABLE outbox (
    outbox_id       BIGINT PRIMARY KEY,
    event_type      VARCHAR(100) NOT NULL,
    payload         JSON NOT NULL COMMENT '이벤트 데이터 (JSON 형식)',
    status          VARCHAR(20) NOT NULL DEFAULT 'READY' COMMENT '상태: READY, PROCEEDING, DONE, FAILED',
    retry_count     INT NOT NULL DEFAULT 0 COMMENT '재시도 횟수',
    next_retry_at   TIMESTAMP NULL COMMENT '다음 재시도 시간',
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Transactional Outbox 테이블';

