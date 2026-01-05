package com.lgcns.bebee.chat.domain.repository;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.MemberSync;
import com.lgcns.bebee.chat.domain.entity.sync.HelpCategorySync;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository {

    Chatroom save(MemberSync member1, MemberSync member2, Long postId, String postTitle, List<HelpCategorySync> helpCategories);

    Optional<Chatroom> findById(Long chatroomId);

    Optional<Chatroom> findChatroom(MemberSync member1, MemberSync member2);

    Optional<Chatroom> findChatroomWithMembers(Long chatroomId);

    Optional<Chatroom> findChatroomWithMembers(MemberSync member1, MemberSync member2, Long postId);

    List<Chatroom> findChatroomsWithCursor(MemberSync member, Long lastChatroomId, int limit);
}
