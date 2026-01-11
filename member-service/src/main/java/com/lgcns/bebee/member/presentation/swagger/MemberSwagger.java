package com.lgcns.bebee.member.presentation.swagger;

import com.lgcns.bebee.member.presentation.dto.res.MemberInfoResDTO;
import com.lgcns.bebee.member.presentation.dto.res.ProfileInfoResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member", description = "회원 관련 API")
public interface MemberSwagger {

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 회원의 기본 정보를 조회합니다.")
    ResponseEntity<MemberInfoResDTO> getMyInfo(Long memberId);

    @Operation(summary = "내 프로필 조회", description = "현재 로그인된 회원의 프로필 정보를 조회합니다.")
    ResponseEntity<ProfileInfoResDTO> getMyProfile(
            @Parameter(hidden = true) Long memberId
    );

    @Operation(summary = "특정 회원 프로필 조회", description = "특정 회원의 프로필 정보를 조회합니다.")
    ResponseEntity<ProfileInfoResDTO> getMemberProfile(
            @Parameter(hidden = true) Long currentMemberId,
            @Parameter(description = "조회할 회원 ID", required = true, example = "1") Long memberId
    );
}
