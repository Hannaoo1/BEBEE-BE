package com.lgcns.bebee.match.domain.repository.dto;

import com.lgcns.bebee.match.domain.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    // helperId + disability_categoryId로 Badge 조회
    Optional<Badge> findByHelperIdAndDisabilityCategoryId(Long helperId, Long disabilityCategoryId);

    // helperId가 가진 모든 Badge 조회
    List<Badge> findAllByHelperId(Long helperId);
}
