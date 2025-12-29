package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.MemberDisabilityCategory;
import com.lgcns.bebee.member.domain.entity.MemberDisabilityCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDisabilityCategoryRepository
        extends JpaRepository<MemberDisabilityCategory, MemberDisabilityCategoryId> {
}
