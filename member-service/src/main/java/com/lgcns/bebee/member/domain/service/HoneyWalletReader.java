package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.member.core.exception.MemberErrors;
import com.lgcns.bebee.member.domain.entity.sync.MemberHoneyWalletSync;
import com.lgcns.bebee.member.domain.repository.HoneyWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HoneyWalletReader {

    private final HoneyWalletRepository honeyWalletRepository;

    @Transactional(readOnly = true)
    public MemberHoneyWalletSync findByMemberId(Long memberId) {
        return honeyWalletRepository.findByMemberId(memberId)
                .orElseThrow(() -> MemberErrors.HONEY_WALLET_NOT_FOUND.toException());
    }
}
