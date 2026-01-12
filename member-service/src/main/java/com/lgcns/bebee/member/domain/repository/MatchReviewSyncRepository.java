package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.sync.MatchReviewSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchReviewSyncRepository extends JpaRepository<MatchReviewSync, Long> {

    /**
     * 특정 회원이 받은 리뷰의 키워드별 개수 조회
     * @param revieweeId 리뷰 받은 회원 ID
     * @return 키워드 ID와 개수
     */
    @Query("""
        SELECT k.keywordId as keywordId, COUNT(k.keywordId) as count
        FROM MatchReviewKeywordSync k
        JOIN k.review r
        WHERE r.revieweeId = :revieweeId
        GROUP BY k.keywordId
        ORDER BY k.keywordId
        """)
    List<KeywordCount> countKeywordsByRevieweeId(@Param("revieweeId") Long revieweeId);

    interface KeywordCount {
        Integer getKeywordId();
        Long getCount();
    }
}
