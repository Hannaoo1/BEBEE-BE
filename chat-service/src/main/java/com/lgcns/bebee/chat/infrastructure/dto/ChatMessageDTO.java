package com.lgcns.bebee.chat.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.bebee.chat.domain.entity.Chat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatMessageDTO(
        String id,
        String senderId,
        String textContent,
        String type,
        List<String> attachments,
        LocalDateTime createdAt,
        MatchConfirmationDTO matchData
) {
    public static ChatMessageDTO from(Long receiverId, Chat chat) {
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
                .createdAt(chat.getCreatedAt())
                .matchData(matchConfirmationDTO)
                .build();
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MatchConfirmationDTO(
            String agreementId,
            String type,
            String receiverId,
            String helperId,
            Boolean isVolunteer,
            Integer unitHoney,
            Integer totalHoney,
            String region,
            List<Long> helpCategoryIds,
            String status,
            EngagementTimeDTO engagementTime
    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record EngagementTimeDTO(
            String date,
            String startDate,
            String endDate,
            ScheduleDTO schedule,
            List<ScheduleDTO> schedules
    ) {
        public static EngagementTimeDTO from(Chat.MatchConfirmationContent content) {
            List<Chat.Schedule> schedules = content.getSchedules();

            if (content.getType() == Chat.EngagementType.DAY) {
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ScheduleDTO(
            String dayOfWeek,
            String startTime,
            String endTime
    ) {
    }
}