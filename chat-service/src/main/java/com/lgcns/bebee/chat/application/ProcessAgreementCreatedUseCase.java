package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.application.client.MessagePublisher;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.sync.MemberSync;
import com.lgcns.bebee.chat.domain.entity.vo.MatchStatus;
import com.lgcns.bebee.chat.domain.repository.ChatRepository;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import com.lgcns.bebee.chat.domain.service.MemberManagement;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.data.dto.ScheduleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessAgreementCreatedUseCase implements UseCase<ProcessAgreementCreatedUseCase.Param, Void>{
    private final MessagePublisher messagePublisher;

    private final ChatroomManagement chatroomManagement;
    private final MemberManagement memberManagement;
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public Void execute(Param param) {
        MemberSync sender = memberManagement.getExistingMember(param.disabledId);
        MemberSync receiver = memberManagement.getExistingMember(param.helperId);

        Chatroom chatroom = chatroomManagement.getExistingChatroom(param.chatroomId);

        Chat chat = Chat.create(
                chatroom.getId(),
                sender.getId(), receiver.getId(),
                null, "MATCH_CONFIRMATION", null,
                param.agreementId, param.engagementType, param.isVolunteer,
                param.startDate, param.endDate, param.schedules,
                param.location, param.unitHoney, param.totalHoney,
                param.helpCategoryIds, MatchStatus.PROCEEDING,
                param.createdAt
        );

        chatRepository.save(chat);
        chatroom.updateMatchStatus(MatchStatus.PROCEEDING);

        messagePublisher.publishToMember(sender.getId(), receiver.getId(), chat);

        chatroomManagement.updateLastMessage(chatroom, chat);

        return null;
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long chatroomId;
        private final Long agreementId;
        private final Long disabledId;
        private final Long helperId;
        private final String engagementType;
        private final Boolean isVolunteer;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final List<ScheduleDTO> schedules;
        private final String location;
        private final Integer unitHoney;
        private final Integer totalHoney;
        private final List<Long> helpCategoryIds;
        private final LocalDateTime createdAt;
    }
}
