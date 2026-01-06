package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.presentation.dto.req.ReviewCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewKeywordResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Review", description = "리뷰 API")
public interface ReviewSwagger {

    @Operation(
            summary = "리뷰 키워드 목록 조회",
            description = """
            리뷰 작성 화면에 필요한 정보를 조회합니다.
            
            **제공 정보:**
            - 게시글 제목
            - 도움 카테고리 목록 (탭)
            - 상대방 닉네임
            - 리뷰 방향에 맞는 키워드 목록
            
            **작성자에 따른 키워드:**
            - 장애인 → 도우미: 키워드 1~12
            - 도우미 → 장애인: 키워드 13~24
            
            **제약사항:**
            - 활동이 완료(COMPLETED) 상태여야 함
            - 참여자만 조회 가능
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "키워드 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "활동 미완료 (ENGAGEMENT_NOT_COMPLETED)"),
            @ApiResponse(responseCode = "403", description = "권한 없음 - 활동 참여자가 아님 (NOT_ENGAGEMENT_MEMBER)"),
            @ApiResponse(responseCode = "404", description = "활동을 찾을 수 없음 (ENGAGEMENT_NOT_FOUND)")
    })
    ResponseEntity<ReviewKeywordResDTO> getReviewKeywordsList(
            @Parameter(hidden = true) @CurrentMember Long currentMemberId,

            @Parameter(description = "활동 ID", example = "123", required = true)
            @RequestParam String engagementId
    );

    @Operation(
            summary = "리뷰 작성",
            description = """
            활동 완료 후 키워드 선택식 리뷰를 작성합니다.
            
            **흐름:**
            1. 활동 완료 확인 (COMPLETED)
            2. 중복 리뷰 확인
            3. 참여자 확인
            4. 리뷰 방향 결정 (DISABLED_TO_HELPER / HELPER_TO_DISABLED)
            5. 키워드 검증 (유효성 + 방향 일치)
            6. 리뷰 저장
            
            **제약사항:**
            - 활동이 완료(COMPLETED) 상태여야 함
            - 한 활동당 1개 리뷰만 작성 가능
            - 최소 1개 이상의 키워드 선택 필수
            - 선택한 키워드는 리뷰 방향과 일치해야 함
            
            **키워드 범위:**
            - 장애인 → 도우미: 1~13
            - 도우미 → 장애인: 14~24
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "리뷰 작성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                잘못된 요청
                - 활동이 완료되지 않음 (ENGAGEMENT_NOT_COMPLETED)
                - 유효하지 않은 키워드 (INVALID_KEYWORD)
                - 키워드 방향 불일치 (KEYWORD_DIRECTION_MISMATCH)
                """
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음 - 활동 참여자가 아님 (NOT_ENGAGEMENT_MEMBER)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "활동을 찾을 수 없음 (ENGAGEMENT_NOT_FOUND)"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복 - 이미 작성한 리뷰 (ALREADY_REVIEWED)"
            )
    })
    ResponseEntity<ReviewCreateResDTO> createReview(
            @Parameter(hidden = true) @CurrentMember Long currentMemberId,

            @RequestBody ReviewCreateReqDTO reqDTO
    );
}