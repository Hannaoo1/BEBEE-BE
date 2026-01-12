package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.application.client.MessagePublisher;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.entity.vo.MatchStatus;
import com.lgcns.bebee.chat.domain.repository.ChatRepository;
import com.lgcns.bebee.chat.domain.service.ChatManagement;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import com.lgcns.bebee.chat.domain.service.MemberManagement;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProcessAgreementRefusedUseCase implements UseCase<ProcessAgreementRefusedUseCase.Param, Void> {
    private final MessagePublisher messagePublisher;

    private final MemberManagement memberManagement;
    private final ChatroomManagement chatroomManagement;
    private final ChatManagement chatManagement;

    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public Void execute(Param param) {
        MemberSync sender = memberManagement.getExistingMember(param.helperId);
        MemberSync receiver = memberManagement.getExistingMember(param.disabledId);

        Chatroom currentChatroom = chatroomManagement.getExistingChatroom(param.chatroomId);
        Chat matchConfirmationChat = chatManagement.getExistingChat(param.chatId);

        Chat chat = Chat.create(
                currentChatroom.getId(),
                sender.getId(), receiver.getId(),
                null, "MATCH_FAILURE",
                null, null, null, null,null, null,
                null,null, null, null, null, null,
                param.createdAt
        );

        matchConfirmationChat.updateMatchStatus(MatchStatus.NON_MATCHED);
        currentChatroom.updateMatchStatus(MatchStatus.NON_MATCHED);

        chatroomManagement.updateLastMessage(currentChatroom, chat);
        chatRepository.save(chat);

        messagePublisher.publishToMember(sender.getId(), receiver.getId(), chat);

        return null;
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long chatroomId;
        private final Long chatId;
        private final Long disabledId;
        private final Long helperId;
        private final LocalDateTime createdAt;
    }
}
