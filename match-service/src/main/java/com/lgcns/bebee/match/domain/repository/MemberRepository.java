package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberSync, Long> {

    @Query(value =
        "SELECT * FROM member_sync m " +
        "WHERE m.role = 'HELPER' and " +
        "ST_Distance_Sphere(POINT(m.longitude, m.latitude), POINT(:lon, :lat)) <= :radius * 1000",
        nativeQuery = true
    )
    List<MemberSync> findHelpersWithinRadius(@Param("lon") double longitude, @Param("lat") double latitude, @Param("radius") int radius);
}
