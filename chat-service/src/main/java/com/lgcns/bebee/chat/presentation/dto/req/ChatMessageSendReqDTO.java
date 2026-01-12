package com.lgcns.bebee.chat.presentation.dto.req;

import java.time.LocalDateTime;
import java.util.List;

public record ChatMessageSendReqDTO(
    String chatroomId,
    String receiverId,
    String textContent,
    String chatType,
    List<String> attachments,
    LocalDateTime createdAt
) {
    public ChatMessageSendReqDTO {
        if(chatType == null) chatType = "TEXT";
    }
}
