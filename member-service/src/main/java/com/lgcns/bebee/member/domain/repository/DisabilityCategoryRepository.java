package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.DisabilityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DisabilityCategoryRepository extends JpaRepository<DisabilityCategory, Long> {
    Optional<DisabilityCategory> findByType(String type);
}
