package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.match.presentation.dto.req.AgreementCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.req.AgreementRefuseReqDTO;
import com.lgcns.bebee.match.presentation.dto.req.HelperApplyReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.res.HelperApplicationPostsGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "지원", description = "활동 지원 관련 API")
public interface HelperApplicationSwagger {

    @Operation(
            summary = "도우미 지원",
            description = "도움 요청 게시글에 대해 도우미가 지원합니다.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HelperApplyReqDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "도우미 지원 요청 예시",
                                            description = "나눔으로 지원할 경우 isVolunteer = true 로 설정",
                                            value = """
                                                {
                                                  "memberId": "101",
                                                  "postId": "1001",
                                                  "isVolunteer": false
                                                }
                                                """
                                    )
                            }
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음"
            )
    })
    ResponseEntity<Void> apply(HelperApplyReqDTO request);

    @Operation(
            summary = "본인 작성 게시글 목록 조회",
            description = "장애인 유저가 본인이 작성한 도움 요청 게시글들을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HelperApplicationPostsGetResDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "본인 작성 게시글 목록 조회 응답 예시",
                                            value = """
                                                    {
                                                        "posts": [
                                                            {
                                                                "postId": 1001,
                                                                "title": "병원 동행 도와주실 분",
                                                                "region": "서울시 강남구 역삼동",
                                                                "commonApplicantCount": 0,
                                                                "volunteerApplicantCount": 0,
                                                                "isMatched": false,
                                                                "daysRemaining": 0,
                                                                "engagementTime": {
                                                                    "date": "2025-12-27",
                                                                    "schedule": {
                                                                        "dayOfWeek": "MONDAY",
                                                                        "startTime": "09:00:00",
                                                                        "endTime": "12:00:00"
                                                                    }
                                                                },
                                                                "helpCategories": [
                                                                    1
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                """
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
    })
    ResponseEntity<HelperApplicationPostsGetResDTO> getHelperApplicationPosts(
            @Parameter(
                    description = "현재 로그인한 회원 ID(임시, 나중에 토큰으로 처리)",
                    required = true,
                    example = "100"
            )
            String memberId
    );
}
