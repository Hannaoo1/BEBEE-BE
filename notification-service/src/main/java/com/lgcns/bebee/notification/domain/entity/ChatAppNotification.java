package com.lgcns.bebee.notification.domain.entity;

import com.lgcns.bebee.notification.domain.entity.vo.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Entity
@PrimaryKeyJoinColumn(name = "app_notification_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CHAT")
public class ChatAppNotification extends AppNotification {
    @Column(nullable = false)
    private Long chatroomId;

    @Column(length = 255)
    private String messagePreview;

    @Column(nullable = false)
    private Integer messageCount = 1;

    public static ChatAppNotification create(
            Long senderId,
            String senderNickname,
            Long receiverId,
            Long chatroomId,
            String messagePreview) {
        ChatAppNotification appNotification = new ChatAppNotification();
        appNotification.senderId = senderId;
        appNotification.senderNickname = senderNickname;
        appNotification.receiverId = receiverId;
        appNotification.type = NotificationType.CHAT;
        appNotification.chatroomId = chatroomId;
        appNotification.messagePreview = messagePreview;
        return appNotification;
    }

    @Override
    public String getTitle() {
        return "새로운 채팅방 생성되었습니다.";
    }

    @Override
    public String getBody() {
        return messagePreview != null ? messagePreview : this.senderNickname + "님과의 대화를 시작해보세요.";
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("type", NotificationType.CHAT.name());
        data.put("senderId", String.valueOf(senderId));
        data.put("receiverId", String.valueOf(receiverId));
        data.put("chatroomId", String.valueOf(chatroomId));
        return data;
    }
}
