package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.match.presentation.dto.req.ReviewCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewKeywordResDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewStatsResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Review", description = "리뷰 관련 API (양방향 키워드 선택형)")
public interface ReviewSwagger {

    @Operation(
            summary = "리뷰 키워드 목록 조회",
            description = """
                활동 완료 후 작성 가능한 키워드 목록을 조회합니다.
                
                **양방향 리뷰:**
                - 장애인이 조회 → 도우미 평가용 키워드 (1-13) 반환
                - 도우미가 조회 → 장애인 평가용 키워드 (14-24) 반환
                
                **테스트용 ID:**
                - 장애인: 100, 200, 400, 600
                - 도우미: 700, 800, 900, 1000
                - 없는 ID는 존재하지 않는 회원 표시
                
                **응답 형식:**
                각 키워드는 ID, 설명(description), 긍정/부정 여부(isPositive)를 포함합니다.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "키워드 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    [
                                        {
                                            "keywordId": 1,
                                            "description": "시간 약속 잘 지켜요",
                                            "isPositive": true
                                        },
                                        {
                                            "keywordId": 2,
                                            "description": "응답 속도 빨라요",
                                            "isPositive": true
                                        }
                                    ]
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음"
            )
    })
    ResponseEntity<List<ReviewKeywordResDTO>> getKeywords(
            @Parameter(description = "리뷰 작성자 회원 ID", required = true, example = "1")
            @RequestParam String currentMemberId
    );

    @Operation(
            summary = "리뷰 작성 (키워드 선택)",
            description = """
                활동 완료 후 키워드를 선택하여 리뷰를 작성합니다.
                
                **양방향 리뷰:**
                - 장애인 작성 → 도우미 평가 (키워드 1-13 사용)
                - 도우미 작성 → 장애인 평가 (키워드 14-24 사용)
                
                **키워드 선택 규칙:**
                - 최소 1개 선택 필수
                
                **작성 조건:**
                - 활동 참여자만 작성 가능
                - 활동 상태가 'COMPLETED'인 경우만 가능
                - 한 활동당 각 참여자는 1번만 작성 가능
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "리뷰 작성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "reviewId": 791168241386394999
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "message": "키워드를 최소 1개 이상 선택해주세요"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음 또는 활동 미완료"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "활동을 찾을 수 없음"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 작성한 리뷰입니다"
            )
    })
    ResponseEntity<ReviewCreateResDTO> createReview(
            @Parameter(description = "리뷰 작성자 회원 ID", required = true, example = "1")
            @RequestParam String currentMemberId,  // ← String!

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "장애인이 도우미 평가",
                                            value = """
                                            {
                                                "engagementId": 791168241386394999,
                                                "keywordIds": [1, 2, 3, 5, 7]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "도우미가 장애인 평가",
                                            value = """
                                            {
                                                "engagementId": 791168241386394999,
                                                "keywordIds": [14, 15, 16, 17]
                                            }
                                            """
                                    )
                            }
                    )
            )
            @Valid @RequestBody ReviewCreateReqDTO reqDTO  // ← @Valid 추가!
    );

    @Operation(
            summary = "받은 후기 통계 조회",
            description = """
                회원이 받은 리뷰의 키워드별 통계를 조회합니다.
                
                **필터링:**
                - 본인 페이지(isMyPage=true): 긍정/부정 모두 표시
                - 타인 프로필(isMyPage=false): 긍정만 표시
                
                **응답:**
                - 키워드별 받은 횟수 집계
                - 횟수 많은 순으로 정렬
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "통계 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "stats": [
                                            {
                                                "keywordId": 5,
                                                "description": "보조 기기 사용에 능숙해요",
                                                "count": 5,
                                                "isPositive": true
                                            },
                                            {
                                                "keywordId": 1,
                                                "description": "시간 약속 잘 지켜요",
                                                "count": 3,
                                                "isPositive": true
                                            }
                                        ]
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음"
            )
    })
    ResponseEntity<ReviewStatsResDTO> getReceivedReviews(
            @Parameter(description = "조회할 회원 ID", required = true, example = "1")
            @PathVariable String memberId,

            @Parameter(description = "본인 페이지 여부 (true: 본인, false: 타인)")
            @RequestParam(defaultValue = "false") boolean isMyPage
    );
}
