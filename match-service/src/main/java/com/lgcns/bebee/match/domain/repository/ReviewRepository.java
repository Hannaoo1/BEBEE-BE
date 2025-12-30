package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 중복 리뷰 확인
    boolean existsByEngagementIdAndReviewerId(Long engagementId, Long reviewerId);
}
