package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 중복 리뷰 확인
    boolean existsByEngagementIdAndReviewerId(Long engagementId, Long reviewerId);

    @Query("SELECT DISTINCT r FROM Review r " +
            "LEFT JOIN FETCH r.keywords " +
            "WHERE r.revieweeId = :revieweeId")
    List<Review> findReceivedReviewsWithKeywords(@Param("revieweeId") Long revieweeId);
}
