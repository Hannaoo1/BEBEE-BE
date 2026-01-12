package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.presentation.dto.req.AgreementCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.req.AgreementRefuseReqDTO;
import com.lgcns.bebee.match.presentation.dto.req.AgreementConfirmReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementConfirmResDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.res.AgreementGetResDTO;
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

@Tag(name="매칭", description = "매칭 관련 API")
public interface AgreementSwagger {

    @Operation(
            summary = "매칭 확인서 생성",
            description = "매칭 확인서를 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "매칭 확인서 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementCreateResDTO.class)
                    )

            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "리소스를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<AgreementCreateResDTO> createAgreement(
            @Parameter(hidden = true)
            @CurrentMember Long currentMemberId,

            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementCreateReqDTO.class),
                            examples = {
                                @ExampleObject(
                                        name = "하루도움 매칭 확인서 생성 요청 예시",
                                        description = """
                                            나눔 활동인 경우 isVolunteer=true로 지정하여 요청
                                            unitHoney, totalHoney는 0 또는 아무 값이나 보내도 됨 (서버에서 자동으로 0으로 처리)
                                        """,
                                        value = """
                                                {
                                                    "postId": 1001,
                                                	"helperId": 700,
                                                    "type": "DAY",
                                                    "isVolunteer": false,
                                                    "helpCategoryIds": [1, 2],
                                                    "unitHoney": 200,
                                                    "totalHoney": 200,
                                                    "region": "서울특별시 중구 장충동",
                                                    "engagementTime": {
                                                        "date": "2025-12-28",
                                                        "schedule": {
                                                            "dayOfWeek": "SUNDAY",
                                                            "startTime": "10:00:00",
                                                            "endTime": "12:00:00"
                                                        }
                                                    },
                                                    "chatroomId": "1",
                                                    "createdAt": "2026-01-15T09:30:00"
                                                }
                                                """
                                ),
                                    @ExampleObject(
                                            name = "지속도움 매칭 확인서 생성 요청 예시",
                                            description = """
                                                나눔 활동인 경우 isVolunteer=true로 지정하여 요청
                                                unitHoney, totalHoney는 0 또는 아무 값이나 보내도 됨 (서버에서 자동으로 0으로 처리)
                                            """,
                                            value = """
                                                    {
                                                         "postId": "1001",
                                                     	 "helperId": "700",
                                                         "disabledId": "100",
                                                         "type": "TERM",
                                                         "isVolunteer": false,
                                                         "helpCategoryIds": [7],
                                                         "unitHoney": 200,
                                                         "totalHoney": 1200,
                                                         "region": "서울특별시 중구 장충동",
                                                          "engagementTime": {
                                                             "startDate": "2026-01-01",
                                                             "endDate": "2026-01-31",
                                                             "schedules": [
                                                                 {
                                                                     "dayOfWeek": "MONDAY",
                                                                     "startTime": "09:00:00",
                                                                     "endTime": "11:00:00"
                                                                 },
                                                                 {
                                                                     "dayOfWeek": "WEDNESDAY",
                                                                     "startTime": "14:00:00",
                                                                     "endTime": "16:00:00"
                                                                 }
                                                             ]
                                                         },
                                                         "chatroomId": "1",
                                                         "chatId":"1",
                                                         "createdAt":"2026-01-15T09:30:00"
                                                     }
                                                """
                                    )
                            }
                    )
            )
            AgreementCreateReqDTO request
    );

    @Operation(
            summary = "매칭 확인서 거절",
            description = "도우미가 매칭 확인서를 거절합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "매칭 확인서 거절 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음"
            )
    })
    ResponseEntity<Void> refuseAgreement(
            @Parameter(hidden = true)
            @CurrentMember Long currentMemberId,

            @Parameter(
                    description = "거절할 매칭 확인서 ID",
                    required = true,
                    example = "791168241386394999"
            )
            String agreementId,

            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementRefuseReqDTO.class),
                            examples = {
                                @ExampleObject(
                                        name = "매칭 확인서 거절 요청 예시",
                                        value = """
                                                {
                                                  "disabledId":"100",
                                                  "chatroomId":"1",
                                                  "chatId":"1",
                                                  "createdAt":"2026-01-15T09:30:00"
                                                }
                                                """
                                )
                            }
                    )
            )
            AgreementRefuseReqDTO request
    );

    @Operation(
            summary = "매칭 확인서 수락",
            description = "도우미가 매칭 확인서를 수락하고 매칭을 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "매칭 확인서 수락 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementConfirmResDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "리소스를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<AgreementConfirmResDTO> confirmAgreement(
            @Parameter(hidden = true)
            @CurrentMember Long currentMemberId,

            @Parameter(
                    description = "수락할 매칭 확인서 ID",
                    required = true,
                    example = "791168241386394999"
            )
            String agreementId,

            @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementConfirmReqDTO.class),
                            examples = {
                                @ExampleObject(
                                        name = "매칭 확인서 수락 예시",
                                        value = """
                                                {
                                                    "disabledId": "100",
                                                    "postId": "1001",
                                                    "title": "식사 보조 도우미분 구해요",
                                                    "chatroomId": "1",
                                                    "chatId":"1",
                                                    "createdAt":"2026-01-15T09:30:00"
                                                }
                                                """
                                )
                            }
                    )
            )
            AgreementConfirmReqDTO request
    );

    @Operation(
            summary = "매칭 확인서 조회",
            description = "매칭 확인서의 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "매칭 확인서 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgreementGetResDTO.class),
                            examples = {
                                @ExampleObject(
                                        name = "하루도움 매칭 확인서 조회 응답 예시",
                                        description = "DAY 타입의 매칭 확인서 조회 결과",
                                        value = """
                                                {
                                                    "agreementId": "791168241386394999",
                                                    "helpType": "DAY",
                                                    "date": "2025-12-28",
                                                    "startDate": null,
                                                    "endDate": null,
                                                    "schedules": [
                                                        {
                                                            "dayOfWeek": "SUNDAY",
                                                            "startTime": "10:00:00",
                                                            "endTime": "12:00:00"
                                                        }
                                                    ],
                                                    "unitHoney": 200,
                                                    "totalHoney": 200,
                                                    "otherId": "700",
                                                    "otherProfileImageUrl": "https://example.com/profile/700.jpg",
                                                    "otherNickname": "도움이",
                                                    "otherGender": "MALE",
                                                    "otherAgeGroup": 20
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "지속도움 매칭 확인서 조회 응답 예시",
                                        description = "TERM 타입의 매칭 확인서 조회 결과",
                                        value = """
                                                {
                                                    "agreementId": "791168241386394999",
                                                    "helpType": "TERM",
                                                    "date": null,
                                                    "startDate": "2026-01-01",
                                                    "endDate": "2026-01-31",
                                                    "schedules": [
                                                        {
                                                            "dayOfWeek": "MONDAY",
                                                            "startTime": "09:00:00",
                                                            "endTime": "11:00:00"
                                                        },
                                                        {
                                                            "dayOfWeek": "WEDNESDAY",
                                                            "startTime": "14:00:00",
                                                            "endTime": "16:00:00"
                                                        }
                                                    ],
                                                    "unitHoney": 200,
                                                    "totalHoney": 1200,
                                                    "otherId": "100",
                                                    "otherProfileImageUrl": "https://example.com/profile/100.jpg",
                                                    "otherNickname": "받는이",
                                                    "otherGender": "FEMALE",
                                                    "otherAgeGroup": 30
                                                }
                                                """
                                )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "리소스를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<AgreementGetResDTO> getAgreement(
            @Parameter(hidden = true)
            @CurrentMember Long memberId,

            @Parameter(
                    description = "조회할 매칭 확인서 ID",
                    required = true,
                    example = "791168241386394999"
            )
            String agreementId
    );
}
