package com.lgcns.bebee.member.domain.repository;

import com.lgcns.bebee.member.domain.entity.sync.MemberHoneyWalletSync;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HoneyWalletRepository extends JpaRepository<MemberHoneyWalletSync, Integer> {

    Optional<MemberHoneyWalletSync> findByMemberId(Long memberId);
}
