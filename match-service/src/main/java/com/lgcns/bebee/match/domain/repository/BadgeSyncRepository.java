package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.sync.BadgeSync;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeSyncRepository extends JpaRepository<BadgeSync, Long> {

    // 도우미 ID와 장애 유형 ID로 뱃지 조회
    Optional<BadgeSync> findByHelperIdAndDisabilityCategoryId(Long helperId, Long disabilityCategoryId);

    // 도우미 ID로 모든 뱃지 조회
    List<BadgeSync> findByHelperId(Long helperId);
}
