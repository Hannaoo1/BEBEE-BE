package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.MemberDisabilityCategory;
import com.lgcns.bebee.member.domain.entity.MemberDisabilityCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberDisabilityCategoryRepository
                extends JpaRepository<MemberDisabilityCategory, MemberDisabilityCategoryId> {
        /**
         * 회원 ID로 장애 정보 조회
         * 
         * @param memberId 회원 ID
         * @return 회원의 장애 정보 목록
         */
        List<MemberDisabilityCategory> findByMember_Id(Long memberId);
}
