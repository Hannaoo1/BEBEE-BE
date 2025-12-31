package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findAllByMemberId(Long memberId);
}