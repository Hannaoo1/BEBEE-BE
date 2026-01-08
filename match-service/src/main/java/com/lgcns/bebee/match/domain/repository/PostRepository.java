package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findAllByMemberIdAndPeriodStartDateGreaterThanEqual(Long memberId, LocalDate currentDate);

    @Query(value =
            "SELECT * FROM post p " +
            "Where ST_Distance_Sphere(POINT(p.longitude, p.latitude), POINT(:lon, :lat)) <= :radius * 1000",
            nativeQuery = true
    )
    List<Post> findPostsWithRadius(@Param("lon") double longitude, @Param("lat") double latitude, @Param("radius") int radius);
}