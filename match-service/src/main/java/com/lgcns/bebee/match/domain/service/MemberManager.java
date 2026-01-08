package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.vo.LocationSearchType;
import com.lgcns.bebee.match.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberManager {
    private final MemberRepository memberSyncRepository;

    @Transactional(readOnly = true)
    public MemberSync findExistingMember(Long memberId) {
        return memberSyncRepository.findById(memberId).orElseThrow(MatchErrors.MEMBER_NOT_FOUND::toException);
    }
}
