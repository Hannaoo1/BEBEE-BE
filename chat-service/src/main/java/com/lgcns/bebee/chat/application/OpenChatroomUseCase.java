package com.lgcns.bebee.chat.application;

import com.lgcns.bebee.chat.domain.entity.Chatroom;
import com.lgcns.bebee.chat.domain.entity.sync.MemberSync;
import com.lgcns.bebee.chat.domain.entity.sync.HelpCategorySync;
import com.lgcns.bebee.chat.domain.service.ChatroomManagement;
import com.lgcns.bebee.chat.domain.service.MemberManagement;
import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenChatroomUseCase implements UseCase<OpenChatroomUseCase.Param, OpenChatroomUseCase.Result> {
    private final ChatroomManagement chatroomManagement;
    private final MemberManagement memberManagement;

    @Override
    @Transactional
    public Result execute(Param param) {
        // 현재 사용자 조회
        MemberSync currentMember = memberManagement.getExistingMember(param.currentMemberId);

        if(param.chatroomId != null){
            Chatroom chatroom = chatroomManagement.findChatroomWithMembers(param.chatroomId);
            MemberSync otherMember = getOtherMember(chatroom, param.currentMemberId);

            return Result.from(chatroom, param.currentMemberId, otherMember);
        }

        // 상대방 조회
        MemberSync otherMember = memberManagement.getExistingMember(param.otherMemberId);

        List<HelpCategorySync> helpCategories = param.helpCategoryIds.stream()
                .map(HelpCategorySync::from)
                .toList();

        Chatroom chatroom = chatroomManagement.findChatroomWithMembers(currentMember, otherMember, param.postId, param.postTitle, helpCategories);
        otherMember = getOtherMember(chatroom, param.currentMemberId);

        return Result.from(chatroom, param.currentMemberId, otherMember);
    }

    private MemberSync getOtherMember(Chatroom chatroom, Long currentMemberId) {
        boolean isCurrentUserMember1 = chatroom.getMember1().getId().equals(currentMemberId);
        return isCurrentUserMember1 ? chatroom.getMember2() : chatroom.getMember1();
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long chatroomId;
        private final Long currentMemberId;
        private final Long otherMemberId;
        private final Long postId;
        private final String postTitle;
        private final List<Long> helpCategoryIds;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result {
        private final Long chatroomId;
        private final Long myId;
        private final Long otherId;
        private final String otherNickname;
        private final String otherProfileImageUrl;
        private final Long postId;
        private final String title;
        private final List<Long> helpCategoryIds;
        private final String matchStatus;

        public static Result from(Chatroom chatroom, Long currentMemberId, MemberSync otherMember) {
            return new Result(
                    chatroom.getId(),
                    currentMemberId,
                    otherMember.getId(),
                    otherMember.getNickname(),
                    otherMember.getProfileImageUrl(),
                    chatroom.getPostId(),
                    chatroom.getTitle(),
                    chatroom.getChatroomHelpCategories().stream().map(chc -> chc.getId().getHelpCategoryId()).toList(),
                    chatroom.getMatchStatus().name()
            );
        }
    }
}