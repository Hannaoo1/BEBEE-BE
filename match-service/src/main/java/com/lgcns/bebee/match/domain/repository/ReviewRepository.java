package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByMatchAndReviewerId(Match match, Long reviewerId);

    Optional<Review> findById(Long reviewId);

    List<Review> findAllByRevieweeId(Long revieweeId);

    List<Review> findAllByReviewerId(Long reviewerId);

    List<Review> findAllByMatch(Match match);
}
