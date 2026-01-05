package com.lgcns.bebee.payment.domain.repository;

import com.lgcns.bebee.payment.domain.entity.HoneyHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HoneyHistoryRepository extends JpaRepository<HoneyHistory, Long> {

    @Query("SELECT hh FROM HoneyHistory hh WHERE hh.honeyWallet.memberId = :memberId ORDER BY hh.createdAt DESC")
    Page<HoneyHistory> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
