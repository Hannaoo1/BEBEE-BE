package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.MemberHelpCategory;
import com.lgcns.bebee.member.domain.entity.MemberHelpCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberHelpCategoryRepository extends JpaRepository<MemberHelpCategory, MemberHelpCategoryId> {
}
