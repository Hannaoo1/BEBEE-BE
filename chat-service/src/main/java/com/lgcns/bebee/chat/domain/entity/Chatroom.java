package com.lgcns.bebee.chat.domain.entity;

import com.lgcns.bebee.chat.domain.entity.sync.HelpCategorySync;
import com.lgcns.bebee.chat.domain.entity.sync.MatchStatusSync;
import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Chatroom extends BaseTimeEntity {
    @Id @Tsid
    @Column(name = "chatroom_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member1_id")
    private MemberSync member1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member2_id")
    private MemberSync member2;

    private Long postId;

    @Column(length = 50)
    private String title;

    @Column(length = 31)
    private String lastMessage;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatroomHelpCategory> chatroomHelpCategories = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MatchStatusSync matchStatus = MatchStatusSync.NON_MATCHED;

    public static Chatroom create(MemberSync member1, MemberSync member2, Long postId, String postTitle, List<HelpCategorySync> helpCategories) {
        Chatroom chatroom = new Chatroom();
        chatroom.member1 = member1;
        chatroom.member2 = member2;
        chatroom.postId = postId;
        chatroom.title = postTitle;
        helpCategories.forEach(category -> {
            ChatroomHelpCategory chatroomHelpCategory = ChatroomHelpCategory.create(chatroom, category);
            chatroom.chatroomHelpCategories.add(chatroomHelpCategory);
        });

        return chatroom;
    }

    public void updateLastMessage(String lastMessage){
        this.lastMessage = lastMessage;
    }
}