package com.lgcns.bebee.chat.domain.entity;

import com.lgcns.bebee.chat.core.exception.ChatInvalidParamErrors;
import com.lgcns.bebee.chat.domain.entity.vo.MatchStatus;
import com.lgcns.bebee.common.data.dto.ScheduleDTO;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Document(collection = "chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat{
    @Id
    private Long id;

    @Field(name = "chatroom_id")
    private Long chatroomId;

    @Field(name = "sender_id")
    private Long senderId;

    @Field(name = "text_content")
    private String textContent;

    private ChatType type;

    private List<String> attachments;

    @Field("match_confirmation_content")
    private MatchConfirmationContent matchConfirmationContent;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Getter
    public static class MatchConfirmationContent {
        private EngagementType type;
        @Field("agreement_id") private Long agreementId;
        @Field("disabled_id") private Long disabledId;
        @Field("helper_id") private Long helperId;
        @Field("is_volunteer") private Boolean isVolunteer;
        @Field("start_date") private String startDate;
        @Field("end_date") private String endDate;
        private List<Schedule> schedules;
        private String region;
        private Points points;
        @Field("help_category_ids") private List<Long> helpCategoryIds;
        private MatchStatus status;
    }

    @Getter
    public static class Schedule{
        private String day;
        @Field("start_time") private String startTime;
        @Field("end_time") private String endTime;
    }

    @Getter
    public static class Points{
        @Field("unit_honey") private Integer unitHoney;
        @Field("total_honey") private Integer totalHoney;
    }

    public enum ChatType {
        TEXT, IMAGE, MATCH_SUCCESS, MATCH_FAILURE, MATCH_CONFIRMATION;

        public static ChatType from(String type){
            try{
                return valueOf(type.toUpperCase());
            }catch(IllegalArgumentException e){
                throw ChatInvalidParamErrors.INVALID_CHAT_TYPE.toException();
            }
        }
    }

    public enum EngagementType {
        DAY, TERM;

        private static EngagementType from(String type){
            try{
                return valueOf(type.toUpperCase());
            }catch(IllegalArgumentException e){
                throw ChatInvalidParamErrors.INVALID_CHAT_TYPE.toException();
            }
        }
    }

    /**
     * Chat 엔티티를 생성하는 정적 팩토리 메서드입니다.
     *
     * @param chatroomId 채팅방 ID
     * @param senderId 발신자 ID
     * @param textContent 텍스트 내용
     * @param chatType 채팅 타입 (String)
     * @param attachments 첨부파일 목록
     * @param agreementId 계약 ID (매칭 확인 시)
     * @param matchType 매칭 타입 (String)
     * @param startDate 일정 시작일 (YYYY.MM.DD 형식)
     * @param endDate 일정 종료일 (YYYY.MM.DD 형식)
     * @param schedules 일정에 대한 요일, 시작 시간, 종료 시간
     * @param region 만남 장소
     * @param unitPoints 단위 포인트
     * @param totalPoints 총 포인트
     * @param status 매칭 상태
     * @param createdAt 채팅 생성 시간
     * @return 생성된 Chat 엔티티
     */
    public static Chat create(
            Long chatroomId,
            Long senderId,
            Long receiverId,
            String textContent,
            String chatType,
            List<String> attachments,
            Long agreementId,
            String matchType,
            Boolean isVolunteer,
            LocalDate startDate,
            LocalDate endDate,
            List<ScheduleDTO> schedules,
            String region,
            Integer unitPoints,
            Integer totalPoints,
            List<Long> helpCategoryIds,
            MatchStatus status,
            LocalDateTime createdAt
    ) {
        Chat chat = new Chat();
        chat.chatroomId = chatroomId;
        chat.senderId = senderId;
        chat.textContent = textContent;
        chat.type = ChatType.from(chatType);
        chat.attachments = attachments;

        // MatchConfirmationContent가 필요한 경우에만 생성
        if (agreementId != null){
            MatchConfirmationContent content = new MatchConfirmationContent();
            content.agreementId = agreementId;
            content.disabledId = senderId;
            content.helperId = receiverId;
            content.type = EngagementType.from(matchType);
            content.isVolunteer = isVolunteer != null && isVolunteer;
            content.startDate = startDate.toString();
            content.endDate = endDate.toString();
            content.schedules = schedules.stream().map(s -> createSchedule(s.dayOfWeek(), s.startTime(), s.endTime())).toList();
            content.region = region;
            content.points = unitPoints != null ? createPoints(unitPoints, totalPoints) : null;
            content.status = status;
            content.helpCategoryIds = helpCategoryIds;
            chat.matchConfirmationContent = content;
        }

        chat.createdAt = createdAt;
        return chat;
    }

    public void updateMatchStatus(MatchStatus status){
        this.matchConfirmationContent.status = status;
    }


    private static Schedule createSchedule(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        Schedule schedule = new Schedule();
        schedule.day = dayOfWeek.name();
        schedule.startTime = startTime.toString();
        schedule.endTime = endTime.toString();
        return schedule;
    }


    private static Points createPoints(Integer unitHoney, Integer totalHoney) {
        Points points = new Points();
        points.unitHoney = unitHoney;
        points.totalHoney = totalHoney;
        return points;
    }


}
