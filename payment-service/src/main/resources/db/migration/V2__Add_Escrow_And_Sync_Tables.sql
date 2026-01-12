SET FOREIGN_KEY_CHECKS = 0;

-- 1. HoneyHistory 테이블 수정
-- balance 컬럼 삭제, amount를 BIGINT로 변경, type enum 값 변경
ALTER TABLE honey_history
    DROP COLUMN balance,
    MODIFY COLUMN amount BIGINT NOT NULL,
    MODIFY COLUMN type ENUM('CHARGE', 'REFUND', 'WITHDRAWL', 'ESCROWED', 'EARNING') NOT NULL;

-- 2. PaymentAgreementSync 테이블 생성
CREATE TABLE payment_agreement_sync (
    agreement_id BIGINT NOT NULL PRIMARY KEY,
    unit_honey INT NOT NULL,
    total_honey INT NOT NULL,
    type ENUM('DAY', 'TERM') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 3. PaymentMatchSync 테이블 생성
CREATE TABLE payment_match_sync (
    match_id BIGINT NOT NULL PRIMARY KEY,
    helper_id BIGINT NOT NULL,
    disabled_id BIGINT NOT NULL,
    agreement_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT UQ_agreement_id UNIQUE (agreement_id),
    CONSTRAINT FK_match_TO_agreement
        FOREIGN KEY (agreement_id)
            REFERENCES payment_agreement_sync (agreement_id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 4. HoneyEscrow 테이블 생성
CREATE TABLE honey_escrow (
    escrow_id BIGINT NOT NULL PRIMARY KEY,
    match_id BIGINT NOT NULL,
    disabled_id BIGINT NOT NULL,
    helper_id BIGINT NOT NULL,
    amount BIGINT NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'REFUNDED') NOT NULL,
    completed_at TIMESTAMP NULL,
    cancelled_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT UQ_match_id UNIQUE (match_id),
    CONSTRAINT FK_escrow_TO_match
        FOREIGN KEY (match_id)
            REFERENCES payment_match_sync (match_id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;