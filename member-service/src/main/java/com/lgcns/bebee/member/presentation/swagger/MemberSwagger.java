package com.lgcns.bebee.member.presentation.swagger;

import com.lgcns.bebee.member.presentation.dto.res.MemberInfoResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member", description = "회원 관련 API")
public interface MemberSwagger {

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 회원의 기본 정보를 조회합니다.")
    ResponseEntity<MemberInfoResDTO> getMyInfo(Long memberId);
}
