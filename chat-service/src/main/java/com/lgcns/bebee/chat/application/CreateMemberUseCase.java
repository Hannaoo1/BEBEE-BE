package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.service.MemberManagement;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateMemberUseCase implements UseCase<CreateMemberUseCase.Param, Void> {
    private final MemberManagement memberManagement;

    @Override
    @Transactional
    public Void execute(Param param) {
        log.info("회원 동기화 생성 시작 - memberId: {}", param.getMemberId());

        memberManagement.createMember(
                param.memberId,
                param.nickname,
                param.profileImageUrl
        );

        log.info("회원 동기화 생성 완료 - memberId: {}", param.getMemberId());

        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final String nickname;
        private final String profileImageUrl;
    }
}
