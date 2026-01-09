package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.presentation.dto.res.MatchCalendarGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.MatchesByDateResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "활동 관리", description = "활동 관리(매칭 현황) 관련 API")
public interface MatchSwagger {

    @Operation(
            summary = "도움 활동 목록 조회",
            description = """
                    선택한 날짜에 활동하는 도움 활동 목록을 조회합니다.
                    
                    **상태 정보(status):**
                    - `INACTIVE`: 활동 완료 활성화 (미래 날짜)
                    - `ACTIVE`: 활동 완료 버튼 활성화 (본인 체크 전)
                    - `COMPLETED`: 활동 완료 (활동 완료 버튼 클릭 후 리뷰를 남기지 않아야 하는 경우)
                    - `REVIEW_ACTIVE`: 리뷰 작성 버튼 활성화 대기(활동 완료 버튼 클릭 후 리뷰를 남겨야 하는 경우)
                    - `REVIEW_COMPLETED`: 리뷰 작성 버튼 비활성화(리뷰 작성 후)
                    
                    *** date ***
                    - 단기 도움의 경우
                    
                    *** dayOfWeeks ***
                    - 단기 도움의 경우 필요하다면 꺼내 사용
                    - 장기 도움의 경우
                    
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "도움 활동 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MatchesByDateResDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "활동 목록 조회 응답 예시",
                                            description = "선택한 날짜의 활동 목록을 반환합니다.",
                                            value = """
                                                    {
                                                        "matches": [
                                                            {
                                                                "engagementId": "123456789",
                                                                "matchId": "234567890",
                                                                "agreementId": "345678901",
                                                                "otherId": "456789012",
                                                                "otherNickname": "도우미닉네임",
                                                                "thumbnailImageUrl": "https://example.com/thumbnail.jpg",
                                                                "title": "도움 요청 제목",
                                                                "chatRoomId": "567890123",
                                                                "region": "서울시 강남구",
                                                                "helpType": "DAY",
                                                                "date": "2026-01-08",
                                                                "dayOfWeeks": ["MONDAY", "WEDNESDAY"],
                                                                "status": "ACTIVE",
                                                                "helpCategoryIds": [1,3,5]
                                                            }
                                                        ]
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효하지 않은 파라미터)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<MatchesByDateResDTO> getMatchesByDate(
            @Parameter(hidden = true)
            @CurrentMember Long memberId,

            @Parameter(
                    description = "캘린더에서 선택한 날짜",
                    required = true,
                    example = "2025-12-07"
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,

            @Parameter(
                    description = "활동 타입(하루도움 = DAY / 지속도움 = TERM / 전체 = null)",
                    example = "DAY"
            )
            @RequestParam(required = false) String type
    );

    @Operation(
            summary = "도움 활동 날짜 조회 (캘린더용)",
            description = "캘린더에서 특정 연도/월 기준으로 도움 활동이 존재하는 날짜 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "활동일 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MatchesByDateResDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "활동일 목록 조회 응답 예시",
                                            description = "하루도움, 지속도움을 모두 포함해 해당 연도/월에 도움 활동이 존재하는 날을 LocalDate 배열 형식으로 반환",
                                            value = """
                                                    {
                                                        "activeDates": [
                                                            "2025-12-01",
                                                            "2025-12-07",
                                                            "2025-12-08",
                                                            "2025-12-15",
                                                            "2025-12-22",
                                                            "2025-12-29",
                                                        ]
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (유효하지 않은 파라미터)",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<MatchCalendarGetResDTO> getActiveDayByMonth(
            @Parameter(hidden = true)
            @CurrentMember Long memberId,

            @Parameter(
                    description = "캘린더에서 선택한 연도",
                    required = true,
                    example = "2025"
            )
            @RequestParam Integer year,

            @Parameter(
                    description = "캘린더에서 선택한 월",
                    required = true,
                    example = "01"
            )
            @RequestParam Integer month
    );
}
