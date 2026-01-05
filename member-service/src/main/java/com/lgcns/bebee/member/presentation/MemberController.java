package com.lgcns.bebee.member.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.member.application.usecase.ReadMemberProfileUseCase;
import com.lgcns.bebee.member.presentation.dto.res.MemberInfoResDTO;
import com.lgcns.bebee.member.presentation.swagger.MemberSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController implements MemberSwagger {

    private final ReadMemberProfileUseCase readMemberProfileUseCase;

    @Override
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResDTO> getMyInfo(@CurrentMember Long memberId) {
        ReadMemberProfileUseCase.Param param = new ReadMemberProfileUseCase.Param(memberId);
        MemberInfoResDTO result = readMemberProfileUseCase.execute(param);
        return ResponseEntity.ok(result);
    }
}
