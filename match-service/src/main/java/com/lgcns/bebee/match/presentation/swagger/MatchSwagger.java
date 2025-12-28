package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.presentation.dto.res.MatchesByDateGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.PostsGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
            description = "선택한 날짜에 활동하는 도움 활동 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "도움 활동 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostsGetResDTO.class)
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
    ResponseEntity<MatchesByDateGetResDTO> getMatchesByDate(
            @Parameter(
                    description = "현재 로그인한 회원 ID(임시, 나중에 토큰으로 처리)",
                    required = true,
                    example = "791168241386394999"
            )
            @RequestParam String memberId,

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
            @RequestParam EngagementType engagementType
    );
}
