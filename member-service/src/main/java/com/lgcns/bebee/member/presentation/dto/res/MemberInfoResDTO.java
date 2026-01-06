package com.lgcns.bebee.member.presentation.dto.res;

import com.lgcns.bebee.member.application.usecase.ReadMemberBaseInfoUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 회원 정보 응답 DTO
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberInfoResDTO {
    private String memberId;
    private String email;
    private String name;
    private String nickname;
    private String role;

    public static MemberInfoResDTO from(ReadMemberBaseInfoUseCase.Result result) {
        return MemberInfoResDTO.builder()
                .memberId(String.valueOf(result.getMemberId()))
                .email(result.getEmail())
                .name(result.getName())
                .nickname(result.getNickname())
                .role(result.getRole().name())
                .build();
    }
}
