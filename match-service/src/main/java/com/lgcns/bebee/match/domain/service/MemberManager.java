package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.sync.*;
import com.lgcns.bebee.match.domain.entity.vo.LocationSearchType;
import com.lgcns.bebee.match.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberManager {
    private final MemberRepository memberSyncRepository;

    @Transactional(readOnly = true)
    public MemberSync findExistingMember(Long memberId) {
        return memberSyncRepository.findById(memberId).orElseThrow(MatchErrors.MEMBER_NOT_FOUND::toException);
    }

    @Transactional
    public MemberSync createMember(
            Long memberId,
            String nickname,
            String gender,
            String role,
            LocalDate birthDate,
            Double latitude,
            Double longitude,
            String profileImageUrl,
            String addressRoad,
            String legalDongCode,
            List<Long> disabilityCategoryIds,
            List<Long> helpCategoryIds
    ){
        List<MemberDisabilityCategorySync> disabilityCategories = disabilityCategoryIds.stream()
                .map(MemberDisabilityCategorySync::create)
                .toList();

        List<MemberHelpCategorySync> helpCategories = helpCategoryIds.stream()
                .map(MemberHelpCategorySync::create)
                .toList();


        MemberSync member = MemberSync.create(
                memberId,
                nickname,
                Gender.valueOf(gender),
                Role.valueOf(role),
                birthDate,
                latitude,
                longitude,
                profileImageUrl,
                addressRoad,
                legalDongCode,
                disabilityCategories,
                helpCategories
        );


        return memberSyncRepository.save(member);
    }
}
