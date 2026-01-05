package com.lgcns.bebee.member.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.member.application.usecase.ReadMemberBaseInfoUseCase;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
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

    private final ReadMemberBaseInfoUseCase readMemberBaseInfoUseCase;
    private final MemberRepository memberRepository;

    @Override
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResDTO> getMyInfo(@CurrentMember Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId=" + memberId));

        ReadMemberBaseInfoUseCase.Param param = new ReadMemberBaseInfoUseCase.Param(member);
        ReadMemberBaseInfoUseCase.Result result = readMemberBaseInfoUseCase.execute(param);

        return ResponseEntity.ok(MemberInfoResDTO.from(result));
    }
}
