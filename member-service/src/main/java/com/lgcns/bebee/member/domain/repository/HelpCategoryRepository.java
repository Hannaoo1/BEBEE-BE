package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.HelpCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HelpCategoryRepository extends JpaRepository<HelpCategory, Long> {
    Optional<HelpCategory> findByHelpType(String helpType);
}
