package com.lgcns.bebee.member.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.member.core.exception.MemberErrors;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.TokenInfo;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import com.lgcns.bebee.member.infrastructure.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Test Auth", description = "인증 테스트 API")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestAuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    /**
     * 현재 로그인한 회원 정보 조회 (인증 필요)
     * @param member 현재 로그인한 회원
     * @return 회원 정보
     */
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponse> getCurrentMember(@CurrentMember Member member) {
        MemberInfoResponse response = new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getRole().name()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * 인증 없이 접근 가능한 엔드포인트
     * @return 공개 메시지
     */
    @GetMapping("/public")
    public ResponseEntity<PublicResponse> getPublic() {
        return ResponseEntity.ok(new PublicResponse("인증 없이 접근 가능합니다."));
    }

    /**
     * 테스트용 임시 토큰 생성 - 장애인 (memberId: 100)
     * @return 액세스 토큰
     */
    @Operation(
            summary = "테스트용 토큰 생성 (장애인)",
            description = """
                    테스트를 위한 임시 JWT 액세스 토큰을 생성합니다.

                    **회원 정보:**
                    - memberId: 100
                    - role: DISABLED (장애인)

                    **사용 방법:**
                    1. 이 API를 호출하여 액세스 토큰을 받습니다
                    2. Swagger UI 상단의 'Authorize' 버튼을 클릭합니다
                    3. 받은 토큰을 입력하여 인증합니다
                    4. 인증이 필요한 다른 API들을 테스트할 수 있습니다

                    **주의:**
                    - 이 API는 테스트 환경에서만 사용해야 합니다
                    - 프로덕션 환경에서는 제거되어야 합니다
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "테스트 회원을 찾을 수 없음 (DB에 memberId=100인 회원이 없음)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/token/disabled")
    public ResponseEntity<TokenResponse> generateTestToken() {
        Long testMemberId = 100L;

        // MemberRepository에서 회원 조회
        Member member = memberRepository.findById(testMemberId)
                .orElseThrow(MemberErrors.MEMBER_NOT_FOUND::toException);

        // JwtTokenProvider를 사용해서 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateTokens(member);

        return ResponseEntity.ok(new TokenResponse(
                tokenInfo.getAccessToken()
        ));
    }

    /**
     * 테스트용 임시 토큰 생성 - 도우미 (memberId: 700)
     * @return 액세스 토큰
     */
    @Operation(
            summary = "테스트용 토큰 생성 (도우미)",
            description = """
                    테스트를 위한 임시 JWT 액세스 토큰을 생성합니다.

                    **회원 정보:**
                    - memberId: 700
                    - role: HELPER (도우미)

                    **사용 방법:**
                    1. 이 API를 호출하여 액세스 토큰을 받습니다
                    2. Swagger UI 상단의 'Authorize' 버튼을 클릭합니다
                    3. 받은 토큰을 입력하여 인증합니다
                    4. 인증이 필요한 다른 API들을 테스트할 수 있습니다

                    **주의:**
                    - 이 API는 테스트 환경에서만 사용해야 합니다
                    - 프로덕션 환경에서는 제거되어야 합니다
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "테스트 회원을 찾을 수 없음 (DB에 memberId=700인 회원이 없음)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/token/helper")
    public ResponseEntity<TokenResponse> generateHelperTestToken() {
        Long testMemberId = 700L;

        // MemberRepository에서 회원 조회
        Member member = memberRepository.findById(testMemberId)
                .orElseThrow(MemberErrors.MEMBER_NOT_FOUND::toException);

        // JwtTokenProvider를 사용해서 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateTokens(member);

        return ResponseEntity.ok(new TokenResponse(
                tokenInfo.getAccessToken()
        ));
    }

    private record MemberInfoResponse(
            Long memberId,
            String email,
            String name,
            String nickname,
            String role
    ) { }

    private record PublicResponse(String message) { }

    private record TokenResponse(
            String accessToken
    ) {}
}

