package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Optional<Badge> findByHelperIdAndDisabilityCategoryId(Long helperId, Long disabilityCategoryId);

    List<Badge> findByHelperId(Long helperId);
}
