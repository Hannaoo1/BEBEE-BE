package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByMatchAndReviewerId(Match match, Long reviewerId);
}
