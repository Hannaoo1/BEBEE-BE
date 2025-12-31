package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.match.presentation.dto.req.ReviewCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewKeywordResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Tag(name = "리뷰", description = "리뷰 관련 API (양방향 키워드 선택형)")
public interface ReviewSwagger {

    @Operation(
            summary = "작성 가능한 키워드 목록 조회",
            description = """
                활동 완료 후 작성 가능한 키워드 목록을 조회합니다.
                
                **양방향 리뷰:**
                - 장애인이 조회 → 도우미 평가용 키워드 (1-13) 반환
                - 도우미가 조회 → 장애인 평가용 키워드 (14-24) 반환
                
                **조회 조건:**
                - 활동 참여자만 조회 가능
                - 활동 상태가 'COMPLETED'인 경우만 가능
                
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
                            schema = @Schema(implementation = ReviewKeywordListResponse.class),
                            examples = @ExampleObject(
                                    name = "키워드 목록 조회 성공 예시",
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
                                        },
                                        {
                                            "keywordId": 9,
                                            "description": "장애 특성에 대한 이해가 전혀 없어요",
                                            "isPositive": false
                                        }
                                    ]
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "참여자 아님",
                                            value = """
                                            {
                                                "message": "활동 참여자만 리뷰를 작성할 수 있습니다"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "활동 미완료",
                                            value = """
                                            {
                                                "message": "완료된 활동만 리뷰를 작성할 수 있습니다"
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "활동을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "message": "활동을 찾을 수 없습니다"
                                    }
                                    """
                            )
                    )
            )
    })
    ResponseEntity<List<ReviewKeywordResDTO>> getKeywords(
            @Parameter(
                    description = "활동 ID",
                    required = true,
                    example = "791168241386394999"
            )
            Long engagementId,

            @Parameter(
                    description = "회원 ID (조회하는 사람)",
                    required = true,
                    example = "100"
            )
            Long memberId
    );

    @Operation(
            summary = "리뷰 작성 (키워드 선택)",
            description = """
                활동 완료 후 키워드를 선택하여 리뷰를 작성합니다.
                
                **양방향 리뷰:**
                - 장애인 작성 → 도우미 평가 (키워드 1-13 사용)
                - 도우미 작성 → 장애인 평가 (키워드 14-24 사용)
                
                **키워드 선택 규칙:**
                - 최소 1개, 최대 10개 선택
                - 리뷰 방향에 맞는 키워드만 선택 가능
                - 모든 키워드는 같은 방향이어야 함
                
                **평가 대상자(revieweeId)는 자동 결정:**
                - 장애인이 작성하면 → 도우미가 평가 대상
                - 도우미가 작성하면 → 장애인이 평가 대상
                
                **작성 조건:**
                - 활동 참여자만 작성 가능
                - 활동 상태가 'COMPLETED'인 경우만 가능
                - 한 활동당 각 참여자는 1번만 작성 가능
                
                **반환값:**
                생성된 리뷰 ID
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "리뷰 작성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewCreateResponse.class),
                            examples = @ExampleObject(
                                    name = "리뷰 작성 성공 예시",
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
                            examples = {
                                    @ExampleObject(
                                            name = "키워드 없음",
                                            value = """
                                            {
                                                "message": "키워드를 최소 1개 이상 선택해주세요"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "키워드 초과",
                                            value = """
                                            {
                                                "message": "키워드는 최대 10개까지 선택 가능합니다"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "유효하지 않은 키워드",
                                            value = """
                                            {
                                                "message": "유효하지 않은 키워드입니다"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "방향 불일치",
                                            value = """
                                            {
                                                "message": "리뷰 방향과 키워드가 일치하지 않습니다"
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "참여자 아님",
                                            value = """
                                            {
                                                "message": "활동 참여자만 리뷰를 작성할 수 있습니다"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "활동 미완료",
                                            value = """
                                            {
                                                "message": "완료된 활동만 리뷰를 작성할 수 있습니다"
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "활동을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "message": "활동을 찾을 수 없습니다"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복 리뷰 작성",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "message": "이미 작성한 리뷰입니다"
                                    }
                                    """
                            )
                    )
            )
    })
    ResponseEntity<ReviewCreateResDTO> createReview(
            @Parameter(
                    description = "회원 ID (작성하는 사람)",
                    required = true,
                    example = "100"
            )
            Long memberId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewCreateReqDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "장애인이 도우미 평가 (키워드 1-13)",
                                            value = """
                                            {
                                                "engagementId": 791168241386394999,
                                                "keywordIds": [1, 2, 3, 5, 7]
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "도우미가 장애인 평가 (키워드 14-24)",
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
            ReviewCreateReqDTO request
    );

    // Swagger 응답 스키마 정의
    @Schema(description = "키워드 목록 응답")
    class ReviewKeywordListResponse {
        @Schema(description = "키워드 목록")
        public List<ReviewKeywordResDTO> keywords;
    }

    @Schema(description = "리뷰 생성 응답")
    class ReviewCreateResponse {
        @Schema(description = "생성된 리뷰 ID", example = "791168241386394999")
        public Long reviewId;
    }
}
