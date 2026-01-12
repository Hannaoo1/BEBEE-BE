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

@Tag(name = "Review", description = "리뷰 API")
public interface ReviewSwagger {

    @Operation(
            summary = "리뷰 키워드 목록 조회",
            description = """
            사용자 역할에 따른 리뷰 키워드 목록을 조회합니다.
            
            **키워드 범위:**
            - 장애인: 키워드 1~13 (도우미 평가용)
            - 도우미: 키워드 14~24 (장애인 평가용)
            
            회원 토큰에서 역할을 자동으로 판단하여 적절한 키워드 목록을 반환합니다.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "키워드 목록 조회 성공")
    })
    ResponseEntity<ReviewKeywordResDTO> getReviewKeywordsList(
            @Parameter(hidden = true) @CurrentMember Long currentMemberId
    );

    @Operation(
            summary = "리뷰 작성",
            description = """
            키워드 선택식 리뷰를 작성합니다.

            **요청 정보:**
            - matchId: 매칭 ID (URL path: /reviews/{matchId})
            - keywordIds: 선택한 키워드 ID 목록 (최소 1개)

            **키워드 범위:**
            - 장애인 → 도우미: 1~13
            - 도우미 → 장애인: 14~24

            **동작 방식:**
            - 회원 토큰에서 작성자 역할을 자동으로 판단하여 리뷰를 생성합니다.
            - 리뷰 대상자는 매칭 정보에서 자동으로 결정됩니다. (상대방)
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "리뷰 작성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (키워드 오류)"
            )
    })
    ResponseEntity<ReviewCreateResDTO> createReview(
            @Parameter(description = "매칭 ID", required = true) String matchId,
            @Parameter(hidden = true) @CurrentMember Long currentMemberId,
            @RequestBody ReviewCreateReqDTO reqDTO
    );
}