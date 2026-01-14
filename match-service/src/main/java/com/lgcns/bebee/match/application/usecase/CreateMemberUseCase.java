package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.domain.service.MemberManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateMemberUseCase implements UseCase<CreateMemberUseCase.Param, Void> {
    private final MemberManager memberManager;

    @Override
    @Transactional
    public Void execute(Param param) {
        log.info("회원 동기화 생성 시작 - memberId: {}", param.getMemberId());

        memberManager.createMember(
                param.getMemberId(),
                param.getNickname(),
                param.getGender(),
                param.getRole(),
                param.getBirthDate(),
                param.getLatitude(),
                param.getLongitude(),
                param.getProfileImageUrl(),
                param.getAddressRoad(),
                param.getLegalDongCode(),
                param.getDisabilityCategoryIds(),
                param.getHelpCategoryIds()
        );

        log.info("회원 동기화 생성 완료 - memberId: {}", param.getMemberId());

        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final String nickname;
        private final String gender;
        private final String role;
        private final LocalDate birthDate;
        private final Double latitude;
        private final Double longitude;
        private final String profileImageUrl;
        private final String addressRoad;
        private final String legalDongCode;
        private final List<Long> disabilityCategoryIds;
        private final List<Long> helpCategoryIds;
    }
}
