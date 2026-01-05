package com.lgcns.bebee.member.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.member.core.exception.MemberErrors;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.TokenInfo;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 테스트용 컨트롤러
 * JWT 필터와 CurrentMember 어노테이션 동작 확인용
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestAuthController {
    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 현재 로그인한 회원 정보 조회 (인증 필요)
     * 
     * @param memberId 현재 로그인한 회원 ID
     * @return 회원 정보
     */
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getCurrentMember(@CurrentMember Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId=" + memberId));

        MemberInfoResponse response = new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getRole().name());
        return ResponseEntity.ok(response);
    }

    /**
     * 인증 없이 접근 가능한 엔드포인트
     * 
     * @return 공개 메시지
     */
    @GetMapping("/public")
    public ResponseEntity<PublicResponse> getPublic() {
        return ResponseEntity.ok(new PublicResponse("인증 없이 접근 가능합니다."));
    }

    /**
     * 테스트용 임시 토큰 생성 - 장애인 (memberId: 100)
     * 
     * @return 액세스 토큰과 리프레시 토큰
     */
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> generateTestToken() {
        Long testMemberId = 100L;

        // MemberRepository에서 회원 조회
        Member member = memberRepository.findById(testMemberId)
                .orElseThrow(MemberErrors.MEMBER_NOT_FOUND::toException);

        // JwtTokenProvider를 사용해서 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateTokens(member);

        return ResponseEntity.ok(new TokenResponse(
                tokenInfo.getAccessToken()));
    }

    private record MemberInfoResponse(
            Long memberId,
            String email,
            String name,
            String nickname,
            String role) {
    }

    private record PublicResponse(String message) {
    }

    private record TokenResponse(
            String accessToken) {
    }
}
