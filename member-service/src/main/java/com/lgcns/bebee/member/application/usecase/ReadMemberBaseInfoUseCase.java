package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadMemberBaseInfoUseCase
        implements UseCase<ReadMemberBaseInfoUseCase.Param, ReadMemberBaseInfoUseCase.Result> {

    @Override
    public Result execute(Param param) {
        Member member = param.getMember();
        return Result.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .role(member.getRole())
                .build();
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements com.lgcns.bebee.common.application.Params {
        private final Member member;
    }

    @Getter
    @Builder
    public static class Result {
        private final Long memberId;
        private final String email;
        private final String name;
        private final String nickname;
        private final Role role;
    }
}
