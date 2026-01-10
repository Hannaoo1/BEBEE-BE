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
            - matchId: 매칭 ID
            - revieweeId: 리뷰 대상자 ID (후기를 받는 회원)
            - keywordIds: 선택한 키워드 ID 목록 (여러 개 가능!)
            
            **검증:**
            - 마지막 활동이 완료된 상태
            - 올바른 키워드 범위
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    ResponseEntity<ReviewCreateResDTO> createReview(
            @Parameter(hidden = true) @CurrentMember Long currentMemberId,
            @RequestBody ReviewCreateReqDTO reqDTO
    );
}