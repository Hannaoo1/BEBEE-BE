package com.lgcns.bebee.chat.presentation.dto.res;

import com.lgcns.bebee.chat.application.OpenChatroomUseCase;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "채팅방 열기 응답 DTO")
public record ChatroomOpenResDTO(
        @Schema(description = "채팅방 ID", example = "1")
        String chatroomId,

        @Schema(description = "본인(현재 사용자) ID", example = "100")
        String myId,

        @Schema(description = "상대방 ID", example = "200")
        String otherId,

        @Schema(description = "상대방 닉네임", example = "홍길동")
        String otherNickname,

        @Schema(description = "상대방 프로필 이미지 URL", example = "https://example.com/profile.jpg")
        String otherProfileImageUrl,

        @Schema(description = "관련 게시글 ID", example = "1001")
        String postId,

        @Schema(description = "채팅방 제목", example = "병원 동행 도와주실 분 구합니다")
        String title,

        @Schema(description = "도움 카테고리 ID 목록", example = "[1, 2, 3]")
        List<Long> helpCategoryIds,

        @Schema(description = "매칭 상태", example = "NON_MATCHED (NON_MATCHED, PROCEEDING, MATCHED 중 하나)")
        String matchStatus
) {

    public static ChatroomOpenResDTO from(OpenChatroomUseCase.Result result) {

        return new ChatroomOpenResDTO(
                String.valueOf(result.getChatroomId()),
                String.valueOf(result.getMyId()),
                String.valueOf(result.getOtherId()),
                result.getOtherNickname(),
                result.getOtherProfileImageUrl(),
                String.valueOf(result.getPostId()),
                result.getTitle(),
                result.getHelpCategoryIds(),
                result.getMatchStatus()
        );
    }
}
