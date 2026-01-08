package com.lgcns.bebee.match.presentation.swagger;

import com.lgcns.bebee.match.presentation.dto.req.NearbyHelpersGetReqDTO;
import com.lgcns.bebee.match.presentation.dto.req.NearbyPostsGetReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.NearbyHelpersGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.NearbyPostsGetResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

@Tag(name = "Map", description = "지도 기반 조회 관련 API")
public interface MapSwagger {

    @Operation(
            summary = "주변 도우미 조회",
            description = """
                    사용자의 현재 위치 또는 등록된 거주지 정보를 기반으로 반경 내 도우미 목록을 조회합니다.

                    **위치 조회 타입:**
                    - CURRENT: 사용자가 입력한 현재 위치(위도, 경도) 기준으로 조회
                    - HOME: 회원 프로필에 등록된 거주지 주소 기준으로 조회

                    **CURRENT 타입 사용 시:**
                    - latitude, longitude 필드 필수
                    - 사용자의 실시간 위치 또는 특정 지점 중심으로 검색

                    **HOME 타입 사용 시:**
                    - 회원 정보에 저장된 주소의 위경도 자동 사용
                    - latitude, longitude 필드 불필요

                    **검색 반경:**
                    - 기본값: 3km
                    - 최소: 1km, 최대: 5km 권장

                    **응답 정보:**
                    - 도우미 ID, 닉네임, 성별, 연령대
                    - 도우미 위치 (위도, 경도)
                    - 제공 가능한 도움 카테고리 목록
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "주변 도우미 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NearbyHelpersGetResDTO.class),
                            examples = @ExampleObject(
                                    name = "주변 도우미 조회 성공 예시",
                                    value = """
                                            {
                                              "nearByHelpers": [
                                                {
                                                  "id": "2001",
                                                  "nickname": "친절한도우미",
                                                  "gender": "FEMALE",
                                                  "ageGroup": 30,
                                                  "latitude": 37.5665,
                                                  "longitude": 126.9780,
                                                  "helpCategories": [1, 3, 5]
                                                },
                                                {
                                                  "id": "2002",
                                                  "nickname": "따뜻한이웃",
                                                  "gender": "MALE",
                                                  "ageGroup": 40,
                                                  "latitude": 37.5675,
                                                  "longitude": 126.9790,
                                                  "helpCategories": [2, 4, 6]
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필수 파라미터 누락, 유효하지 않은 값 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "잘못된 요청 예시",
                                    value = """
                                            {
                                              "error": "BAD_REQUEST",
                                              "message": "type이 CURRENT일 경우 latitude와 longitude는 필수입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "회원 없음 예시",
                                    value = """
                                            {
                                              "error": "NOT_FOUND",
                                              "message": "회원 정보를 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<NearbyHelpersGetResDTO> getNearbyHelpers(
            @Parameter(
                    hidden = true,
                    description = "현재 로그인한 회원 ID (자동 주입)"
            )
            Long currentMemberId,

            @Parameter(
                    description = """
                            주변 도우미 조회 파라미터

                            **필수 필드:**
                            - type: 위치 조회 기준 (CURRENT | HOME)

                            **CURRENT 타입 사용 시 필수:**
                            - latitude: 위도 (예: 37.5665)
                            - longitude: 경도 (예: 126.9780)

                            **선택 필드:**
                            - radius: 검색 반경 (km, 기본값: 3)
                            """,
                    required = true,
                    schema = @Schema(implementation = NearbyHelpersGetReqDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "CURRENT 타입 (현재 위치 기준)",
                                    value = """
                                            {
                                              "type": "CURRENT",
                                              "latitude": 37.5665,
                                              "longitude": 126.9780,
                                              "radius": 5
                                            }
                                            """,
                                    description = "사용자가 입력한 위경도를 기준으로 반경 5km 내 도우미 검색"
                            ),
                            @ExampleObject(
                                    name = "HOME 타입 (등록된 주소 기준)",
                                    value = """
                                            {
                                              "type": "HOME",
                                              "radius": 3
                                            }
                                            """,
                                    description = "회원 프로필에 등록된 거주지를 기준으로 반경 3km 내 도우미 검색"
                            )
                    }
            )
            @ModelAttribute NearbyHelpersGetReqDTO reqDTO
    );

    @Operation(
            summary = "주변 게시글 조회",
            description = """
                    사용자의 현재 위치 또는 등록된 거주지 정보를 기반으로 반경 내 도움 요청 게시글 목록을 조회합니다.

                    **위치 조회 타입:**
                    - CURRENT: 사용자가 입력한 현재 위치(위도, 경도) 기준으로 조회
                    - HOME: 회원 프로필에 등록된 거주지 주소 기준으로 조회

                    **CURRENT 타입 사용 시:**
                    - latitude, longitude 필드 필수
                    - 사용자의 실시간 위치 또는 특정 지점 중심으로 검색

                    **HOME 타입 사용 시:**
                    - 회원 정보에 저장된 주소의 위경도 자동 사용
                    - latitude, longitude 필드 불필요

                    **검색 반경:**
                    - 기본값: 3km
                    - 최소: 1km, 최대: 5km 권장

                    **응답 정보:**
                    - 게시글 ID, 제목, 법정동 이름
                    - 도움 카테고리 목록
                    - 활동 날짜 및 요일 정보
                    - 게시글 위치 (위도, 경도)

                    **활용 사례:**
                    - 지도 화면에서 주변의 도움 요청 게시글 표시
                    - 사용자 근처의 매칭 가능한 게시글 탐색
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "주변 게시글 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NearbyPostsGetResDTO.class),
                            examples = @ExampleObject(
                                    name = "주변 게시글 조회 성공 예시",
                                    value = """
                                            {
                                              "nearByHelpers": [
                                                {
                                                  "postId": 1001,
                                                  "title": "외출 동행 도와주실 분 구합니다",
                                                  "legalDongName": "역삼동",
                                                  "helpCategories": [1, 3],
                                                  "date": "2026-01-15",
                                                  "dayOfWeeks": [],
                                                  "latitude": 37.5665,
                                                  "longitude": 126.9780
                                                },
                                                {
                                                  "postId": 1002,
                                                  "title": "정기적 외출 지원 필요합니다",
                                                  "legalDongName": "청담동",
                                                  "helpCategories": [1, 5],
                                                  "date": null,
                                                  "dayOfWeeks": ["MONDAY", "WEDNESDAY", "FRIDAY"],
                                                  "latitude": 37.5675,
                                                  "longitude": 126.9790
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필수 파라미터 누락, 유효하지 않은 값 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "잘못된 요청 예시",
                                    value = """
                                            {
                                              "error": "BAD_REQUEST",
                                              "message": "type이 CURRENT일 경우 latitude와 longitude는 필수입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "회원을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "회원 없음 예시",
                                    value = """
                                            {
                                              "error": "NOT_FOUND",
                                              "message": "회원 정보를 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json")
            )
    })
    ResponseEntity<NearbyPostsGetResDTO> getNearbyPosts(
            @Parameter(
                    hidden = true,
                    description = "현재 로그인한 회원 ID (자동 주입)"
            )
            Long currentMemberId,

            @Parameter(
                    description = """
                            주변 게시글 조회 파라미터

                            **필수 필드:**
                            - type: 위치 조회 기준 (CURRENT | HOME)

                            **CURRENT 타입 사용 시 필수:**
                            - latitude: 위도 (예: 37.5665)
                            - longitude: 경도 (예: 126.9780)

                            **선택 필드:**
                            - radius: 검색 반경 (km, 기본값: 3)
                            """,
                    required = true,
                    schema = @Schema(implementation = NearbyPostsGetReqDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "CURRENT 타입 (현재 위치 기준)",
                                    value = """
                                            {
                                              "type": "CURRENT",
                                              "latitude": 37.5665,
                                              "longitude": 126.9780,
                                              "radius": 5
                                            }
                                            """,
                                    description = "사용자가 입력한 위경도를 기준으로 반경 5km 내 게시글 검색"
                            ),
                            @ExampleObject(
                                    name = "HOME 타입 (등록된 주소 기준)",
                                    value = """
                                            {
                                              "type": "HOME",
                                              "radius": 3
                                            }
                                            """,
                                    description = "회원 프로필에 등록된 거주지를 기준으로 반경 3km 내 게시글 검색"
                            )
                    }
            )
            @ModelAttribute NearbyPostsGetReqDTO reqDTO
    );
}
