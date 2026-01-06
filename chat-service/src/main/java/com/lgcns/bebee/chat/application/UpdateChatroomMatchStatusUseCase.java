package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.sync.MatchStatusSync;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateChatroomMatchStatusUseCase implements UseCase<UpdateChatroomMatchStatusUseCase.Param, Void> {
    private final ChatroomManagement chatroomManagement;

    @Override
    public Void execute(Param params) {
        Chatroom chatroom = chatroomManagement.getExistingChatroom(params.chatroomId);

        chatroom.updateMatchStatus(params.matchStatus);

        return null;
    }

    @RequiredArgsConstructor
    public static class Param implements Params{
        private final Long chatroomId;
        private final MatchStatusSync matchStatus;
    }
}
