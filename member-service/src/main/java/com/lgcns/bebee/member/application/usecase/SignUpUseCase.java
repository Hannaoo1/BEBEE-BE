package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.repository.MemberRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

import com.lgcns.bebee.member.domain.entity.DisabilityCategory;
import com.lgcns.bebee.member.domain.entity.HelpCategory;
import com.lgcns.bebee.member.domain.entity.MemberDisabilityCategory;
import com.lgcns.bebee.member.domain.entity.MemberHelpCategory;
import com.lgcns.bebee.member.domain.repository.DisabilityCategoryRepository;
import com.lgcns.bebee.member.domain.repository.HelpCategoryRepository;
import com.lgcns.bebee.member.domain.repository.MemberDisabilityCategoryRepository;
import com.lgcns.bebee.member.domain.repository.MemberHelpCategoryRepository;
import com.lgcns.bebee.member.domain.service.MemberManagement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpUseCase implements UseCase<SignUpUseCase.Param, SignUpUseCase.Result> {
    private final MemberRepository memberRepository;
    private final MemberManagement memberManagement;
    private final HelpCategoryRepository helpCategoryRepository;
    private final DisabilityCategoryRepository disabilityCategoryRepository;
    private final MemberHelpCategoryRepository memberHelpCategoryRepository;
    private final MemberDisabilityCategoryRepository memberDisabilityCategoryRepository;

    @Override
    @Transactional
    public SignUpUseCase.Result execute(Param params) {
        params.validate();

        memberManagement.checkEmailDuplicated(params.getEmail());
        memberManagement.checkNicknameDuplicated(params.getNickname());

        Member newMember = memberManagement.createMember(
                params.getEmail(),
                params.getPassword(),
                params.getName(),
                params.getNickname(),
                params.getBirthDate(),
                params.getGender(),
                params.getPhoneNumber(),
                params.getRole(),
                params.getAddressRoad(),
                params.getLatitude(),
                params.getLongitude(),
                params.getDistrictCode());

        Member savedMember = memberRepository.save(newMember);

        // HELPER: 도움 유형 저장
        if ("HELPER".equals(params.getRole()) && params.getHelpTypes() != null && !params.getHelpTypes().isEmpty()) {
            for (String helpTypeName : params.getHelpTypes()) {
                HelpCategory helpCategory = helpCategoryRepository
                        .findByHelpType(helpTypeName)
                        .orElseThrow(() -> new IllegalArgumentException("도움 유형을 찾을 수 없습니다: " + helpTypeName));
                MemberHelpCategory memberHelpCategory = MemberHelpCategory.create(savedMember, helpCategory);
                memberHelpCategoryRepository.save(memberHelpCategory);
            }
        }

        // DISABLED: 장애 유형 저장
        if ("DISABLED".equals(params.getRole()) && params.getDisabilityType() != null
                && !params.getDisabilityType().isBlank()) {
            DisabilityCategory disabilityCategory = disabilityCategoryRepository
                    .findByType(params.getDisabilityType())
                    .orElseThrow(() -> new IllegalArgumentException("장애 유형을 찾을 수 없습니다: " + params.getDisabilityType()));
            MemberDisabilityCategory memberDisabilityCategory = MemberDisabilityCategory.create(
                    savedMember,
                    disabilityCategory,
                    "1", // 기본 등급 (TODO: 프론트에서 받아오도록 수정 필요)
                    params.getDisabilityDescription() != null ? params.getDisabilityDescription() : "");
            memberDisabilityCategoryRepository.save(memberDisabilityCategory);
        }

        return new Result(savedMember.getId());
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final String email;
        private final String password;
        private final String name;
        private final String nickname;
        private final LocalDate birthDate;
        private final String gender;
        private final String phoneNumber;
        private final String role;
        private final String addressRoad;
        private final BigDecimal latitude;
        private final BigDecimal longitude;
        private final String districtCode;

        // HELPER용: 도움 유형 목록
        private final java.util.List<String> helpTypes;

        // DISABLED용: 장애 유형 및 설명
        private final String disabilityType;
        private final String disabilityDescription;

        @Override
        public boolean validate() {
            validateEmail();
            validateNickname();
            validatePassword();
            return true;
        }

        private void validateEmail() {
            if (email == null || email.isBlank()) {
                throw MemberInvalidParamErrors.EMAIL_NOT_NULL.toException();
            }
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
            if (!emailPattern.matcher(email).matches()) {
                throw MemberInvalidParamErrors.INVALID_EMAIL_FORMAT.toException();
            }
        }

        private void validateNickname() {
            if (nickname == null || nickname.isBlank()) {
                throw MemberInvalidParamErrors.NICKNAME_NOT_NULL.toException();
            }

        }

        private void validatePassword() {
            if (password == null || password.isBlank()) {
                throw MemberInvalidParamErrors.PASSWORD_NOT_NULL.toException();
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result {
        private final Long memberId;
    }

    public boolean checkEmailDuplicated(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean checkNicknameDuplicated(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
// Force git tracking for conflict resolution
