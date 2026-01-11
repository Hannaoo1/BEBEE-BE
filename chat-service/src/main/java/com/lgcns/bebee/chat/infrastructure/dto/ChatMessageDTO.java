package com.lgcns.bebee.chat.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.common.data.dto.ScheduleDTO;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatMessageDTO(
        Long id,
        Long chatroomId,
        Long senderId,
        Long receiverId,
        String textContent,
        String chatType,
        List<String> attachments,
        Long agreementId,
        Long disabledId,
        Long helperId,
        Boolean isVolunteer,
        String matchType,
        String startDate,
        String endDate,
        List<ScheduleDTO> schedules,
        String region,
        Integer unitPoints,
        Integer totalPoints,
        List<Long> helpCategoryIds,
        String matchStatus,
        LocalDateTime createdAt
){
    public static ChatMessageDTO from(Long receiverId, Chat chat) {
        Chat.MatchConfirmationContent matchConfirmation = chat.getMatchConfirmationContent();

        // MatchConfirmationContent가 없는 경우 처리
        Long agreementId = null;
        Long disabledId = null;
        Long helperId = null;
        Boolean isVolunteer = null;
        String matchType = null;
        String startDate = null;
        String endDate = null;
        List<ScheduleDTO> schedules = null;
        String region = null;
        Integer unitHoney = null;
        Integer totalHoney = null;
        List<Long> helpCategoryIds = null;
        String matchStatus = null;

        if (matchConfirmation != null) {
            agreementId = matchConfirmation.getAgreementId();
            disabledId = matchConfirmation.getDisabledId();
            helperId = matchConfirmation.getHelperId();
            isVolunteer = matchConfirmation.getIsVolunteer();
            matchType = matchConfirmation.getType() != null ? matchConfirmation.getType().name() : null;
            startDate = matchConfirmation.getStartDate();
            endDate = matchConfirmation.getEndDate();
            region = matchConfirmation.getRegion();
            helpCategoryIds = matchConfirmation.getHelpCategoryIds();
            matchStatus = matchConfirmation.getStatus() != null
                    ? matchConfirmation.getStatus().name()
                    : null;

            // Schedule 리스트 변환
            List<Chat.Schedule> chatSchedules = matchConfirmation.getSchedules();
            if (chatSchedules != null && !chatSchedules.isEmpty()) {
                schedules = chatSchedules.stream()
                        .map(s -> new ScheduleDTO(
                                DayOfWeek.valueOf(s.getDay()),
                                LocalTime.parse(s.getStartTime()),
                                LocalTime.parse(s.getEndTime())
                        ))
                        .toList();
            }

            // Points 분해
            Chat.Points points = matchConfirmation.getPoints();
            if (points != null) {
                unitHoney = points.getUnitHoney();
                totalHoney = points.getTotalHoney();
            }
        }

        return new ChatMessageDTO(
                chat.getId(),
                chat.getChatroomId(),
                chat.getSenderId(),
                receiverId,
                chat.getTextContent(),
                chat.getType().name(),
                chat.getAttachments(),
                agreementId,
                disabledId,
                helperId,
                isVolunteer,
                matchType,
                startDate,
                endDate,
                schedules,
                region,
                unitHoney,
                totalHoney,
                helpCategoryIds,
                matchStatus,
                chat.getCreatedAt()
        );
    }
}