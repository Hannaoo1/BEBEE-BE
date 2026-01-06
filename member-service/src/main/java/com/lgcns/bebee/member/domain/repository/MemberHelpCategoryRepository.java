package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.MemberHelpCategory;
import com.lgcns.bebee.member.domain.entity.MemberHelpCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberHelpCategoryRepository extends JpaRepository<MemberHelpCategory, MemberHelpCategoryId> {
    /**
     * 회원 ID로 도움 유형 목록 조회
     * 
     * @param memberId 회원 ID
     * @return 회원의 도움 유형 목록
     */
    List<MemberHelpCategory> findByMember_Id(Long memberId);
}
