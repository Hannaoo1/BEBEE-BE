package com.lgcns.bebee.chat.presentation.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.bebee.chat.domain.entity.Chat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "채팅 메시지 목록 조회 응답")
public record ChatMessagesGetResDTO(
        @Schema(description = "채팅 메시지 목록", example = "[...]")
        List<ChatMessageDTO> messages,

        @Schema(description = "다음 페이지 존재 여부", example = "true")
        boolean hasNext,

        @Schema(description = "다음 페이지 조회를 위한 커서 ID (hasNext가 true일 때만 제공)", example = "1234567890", nullable = true)
        String nextChatId
) {
    public static ChatMessagesGetResDTO from(List<Chat> messages, boolean hasNext, Long nextChatId) {
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(ChatMessageDTO::from)
                .toList();

        return ChatMessagesGetResDTO.builder()
                .messages(messageDTOs)
                .hasNext(hasNext)
                .nextChatId(nextChatId != null ? String.valueOf(nextChatId) : null)
                .build();
    }

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "채팅 메시지 상세 정보")
    public record ChatMessageDTO(
            @Schema(description = "메시지 ID (TSID)", example = "1234567890")
            String id,

            @Schema(description = "발신자 ID", example = "100")
            String senderId,

            @Schema(description = "텍스트 메시지 내용", example = "안녕하세요", nullable = true)
            String textContent,

            @Schema(description = "메시지 타입", example = "TEXT", allowableValues = {"TEXT", "IMAGE", "MATCH_CONFIRMATION", "MATCH_SUCCESS", "MATCH_FAILURE"})
            String type,

            @Schema(description = "첨부 파일 URL 목록", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]", nullable = true)
            List<String> attachments,

            MatchConfirmationDTO matchData
    ) {
        public static ChatMessageDTO from(Chat chat) {
            MatchConfirmationDTO matchConfirmationDTO = null;

            if (chat.getMatchConfirmationContent() != null) {
                Chat.MatchConfirmationContent content = chat.getMatchConfirmationContent();
                Chat.Points points = content.getPoints();

                matchConfirmationDTO = new MatchConfirmationDTO(
                        content.getAgreementId() != null ? String.valueOf(content.getAgreementId()) : null,
                        content.getType() != null ? content.getType().name() : null,
                        content.getDisabledId() != null ? String.valueOf(content.getDisabledId()) : null,
                        content.getHelperId() != null ? String.valueOf(content.getHelperId()) : null,
                        content.getIsVolunteer(),
                        points != null ? points.getUnitHoney() : null,
                        points != null ? points.getTotalHoney() : null,
                        content.getRegion(),
                        content.getHelpCategoryIds(),
                        content.getStatus() != null ? content.getStatus().name() : null,
                        EngagementTimeDTO.from(content)
                );
            }

            return ChatMessageDTO.builder()
                    .id(chat.getId() != null ? String.valueOf(chat.getId()) : null)
                    .senderId(chat.getSenderId() != null ? String.valueOf(chat.getSenderId()) : null)
                    .textContent(chat.getTextContent())
                    .type(chat.getType() != null ? chat.getType().name() : null)
                    .attachments(chat.getAttachments())
                    .matchData(matchConfirmationDTO)
                    .build();
        }
    }

    @Schema(description = "매칭 확인서 정보 (type이 MATCH_CONFIRMATION인 경우에만 존재)")
    public record MatchConfirmationDTO(
            @Schema(description = "매칭 확인서 ID", example = "5001", nullable = true)
            String agreementId,

            @Schema(description = "매칭 타입", example = "TERM", allowableValues = {"DAY", "TERM"}, nullable = true)
            String type,

            @Schema(description = "도움 요청자 ID", example = "100", nullable = true)
            String receiverId,

            @Schema(description = "도우미 ID", example = "700", nullable = true)
            String helperId,

            @Schema(description = "나눔 여부 (true: 봉사, false: 유료)", example = "false", nullable = true)
            Boolean isVolunteer,

            @Schema(description = "단위 포인트 (시간당 허니)", example = "1000", nullable = true)
            Integer unitHoney,

            @Schema(description = "총 포인트 (총 허니)", example = "5000", nullable = true)
            Integer totalHoney,

            @Schema(description = "만남 장소", example = "서울시 강남구 역삼동", nullable = true)
            String region,

            @Schema(description = "도움 카테고리 ID 목록", example = "[1, 2, 3]", nullable = true)
            List<Long> helpCategoryIds,

            @Schema(description = "매칭 상태", example = "PROCEEDING", allowableValues = {"PROCEEDING", "MATCHED", "NON_MATCHED"}, nullable = true)
            String status,

            @Schema(description = "활동 일정 정보", nullable = true)
            EngagementTimeDTO engagementTime
    ){
    }

    @Schema(description = "활동 일정 정보")
    public record EngagementTimeDTO(
            @Schema(description = "활동 날짜 (DAY 타입인 경우)", example = "2024-01-15", nullable = true)
            String date,

            @Schema(description = "활동 시작일 (TERM 타입인 경우)", example = "2024-01-15", nullable = true)
            String startDate,

            @Schema(description = "활동 종료일 (TERM 타입인 경우)", example = "2024-02-15", nullable = true)
            String endDate,

            @Schema(description = "단일 스케줄 (DAY 타입인 경우)", nullable = true)
            ScheduleDTO schedule,

            @Schema(description = "스케줄 목록 (TERM 타입인 경우)", nullable = true)
            List<ScheduleDTO> schedules
    ){
        public static EngagementTimeDTO from(Chat.MatchConfirmationContent content) {
            List<Chat.Schedule> schedules = content.getSchedules();

            if(content.getType() == Chat.EngagementType.DAY){
                Chat.Schedule schedule = schedules.get(0);

                return new EngagementTimeDTO(content.getStartDate(), null, null,
                        new ScheduleDTO(schedule.getDay(), schedule.getStartTime(), schedule.getEndTime()), null);
            }
            return new EngagementTimeDTO(null, content.getStartDate(), content.getEndDate(),
                    null, schedules.stream()
                    .map(s -> new ScheduleDTO(s.getDay(), s.getStartTime(), s.getEndTime()))
                    .toList()
            );
        }
    }

    @Schema(description = "스케줄 정보")
    public record ScheduleDTO(
            @Schema(description = "요일", example = "MONDAY", allowableValues = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"})
            String dayOfWeek,

            @Schema(description = "시작 시간", example = "09:00")
            String startTime,

            @Schema(description = "종료 시간", example = "18:00")
            String endTime
    ){
    }
}