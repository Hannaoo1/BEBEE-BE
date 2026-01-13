package com.lgcns.bebee.common.data.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    @Query(value = "SELECT * FROM outbox o WHERE o.status = 'READY' " +
            "AND (o.next_retry_at IS NULL OR o.next_retry_at <= :now) " +
            "AND o.retry_count < :maxRetryCount " +
            "ORDER BY o.created_at ASC " +
            "LIMIT :limit " +
            "FOR UPDATE SKIP LOCKED",
            nativeQuery = true)
    List<Outbox> findRetryableOutboxesWithLock(@Param("now") LocalDateTime now,
                                               @Param("maxRetryCount") int maxRetryCount,
                                               @Param("limit") int limit);

    @Query("SELECT o FROM Outbox o WHERE o.status = 'DONE' AND o.createdAt < :before")
    List<Outbox> findCompletedOutboxesBefore(@Param("before") LocalDateTime before);
}

