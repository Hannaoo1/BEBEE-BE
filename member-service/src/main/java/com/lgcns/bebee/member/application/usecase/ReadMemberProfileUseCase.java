package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.util.AgeGroupCalculator;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.MemberDisabilityCategory;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import com.lgcns.bebee.member.domain.repository.MemberHelpCategoryRepository;
import com.lgcns.bebee.member.domain.repository.MemberDisabilityCategoryRepository;
import com.lgcns.bebee.member.domain.repository.DocumentVerificationRepository;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 마이페이지 회원 프로필 조회 UseCase
 */
@Service
@RequiredArgsConstructor
public class ReadMemberProfileUseCase
        implements UseCase<ReadMemberProfileUseCase.Param, ReadMemberProfileUseCase.Result> {

    private final MemberRepository memberRepository;
    private final MemberHelpCategoryRepository memberHelpCategoryRepository;
    private final MemberDisabilityCategoryRepository memberDisabilityCategoryRepository;
    private final DocumentVerificationRepository documentVerificationRepository;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Param param) {
        // 1. 회원 조회
        Member member = memberRepository.findById(param.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId=" + param.getMemberId()));

        // 2. 연령대 계산
        Integer ageGroup = AgeGroupCalculator.calculateAgeGroup(member.getBirthDate());

        // 3. 역할별 추가 정보 조회
        List<String> helpTypes = null;
        List<DocumentVerification> documents = null;
        String disabilityType = null;
        String disabilityDescription = null;

        if (Role.HELPER.equals(member.getRole())) {
            // 도우미: helpTypes, documents 조회
            helpTypes = memberHelpCategoryRepository.findByMember_Id(member.getId())
                    .stream()
                    .map(mhc -> mhc.getHelpCategory().getHelpType())
                    .collect(Collectors.toList());

            documents = documentVerificationRepository.findByMemberId(member.getId());

        } else if (Role.DISABLED.equals(member.getRole())) {
            // 장애인: disabilityType, disabilityDescription 조회
            List<MemberDisabilityCategory> disabilityCategories = memberDisabilityCategoryRepository
                    .findByMember_Id(member.getId());

            if (!disabilityCategories.isEmpty()) {
                MemberDisabilityCategory firstCategory = disabilityCategories.get(0);
                disabilityType = firstCategory.getDisabilityCategory().getType();
                disabilityDescription = firstCategory.getDisabilityDescription();
            }
        }

        return Result.builder()
                .member(member)
                .ageGroup(ageGroup)
                .helpTypes(helpTypes)
                .documents(documents)
                .disabilityType(disabilityType)
                .disabilityDescription(disabilityDescription)
                .build();
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
    }

    @Getter
    @Builder
    public static class Result {
        private final Member member;
        private final Integer ageGroup;
        private final List<String> helpTypes;
        private final List<DocumentVerification> documents;
        private final String disabilityType;
        private final String disabilityDescription;
    }
}
