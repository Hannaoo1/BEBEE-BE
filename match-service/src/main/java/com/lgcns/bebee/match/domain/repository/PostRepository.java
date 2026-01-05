package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findAllByMemberIdAndPeriodStartDateGreaterThanEqual(Long memberId, LocalDate currentDate);
}