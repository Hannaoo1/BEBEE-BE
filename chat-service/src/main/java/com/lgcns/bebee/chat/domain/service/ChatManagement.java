package com.lgcns.bebee.chat.domain.service;

import com.lgcns.bebee.chat.core.exception.ChatErrors;
import com.lgcns.bebee.chat.domain.entity.Chat;
import com.lgcns.bebee.chat.domain.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatManagement {
    private final ChatRepository chatRepository;

    @Transactional(readOnly = true)
    public Chat getExistingChat(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(ChatErrors.CHAT_NOT_FOUND::toException);
    }
}
