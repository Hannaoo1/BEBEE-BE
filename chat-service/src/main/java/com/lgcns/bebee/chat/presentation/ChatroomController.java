package com.lgcns.bebee.chat.presentation;

import com.lgcns.bebee.chat.application.GetChatMessagesUseCase;
import com.lgcns.bebee.chat.application.GetChatroomsUseCase;
import com.lgcns.bebee.chat.application.OpenChatroomUseCase;
import com.lgcns.bebee.chat.presentation.dto.req.ChatroomOpenReqDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatMessagesGetResDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatroomOpenResDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatroomsGetResDTO;
import com.lgcns.bebee.chat.presentation.swagger.ChatroomSwagger;
import com.lgcns.bebee.common.annotation.CurrentMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatroomController implements ChatroomSwagger {
    private final GetChatMessagesUseCase getChatMessagesUseCase;
    private final GetChatroomsUseCase getChatroomsUseCase;
    private final OpenChatroomUseCase openChatroomUseCase;

    @GetMapping("/chats")
    public ResponseEntity<ChatMessagesGetResDTO> getChatMessages(
            @RequestParam String chatroomId,
            @RequestParam(required = false) String lastChatId,
            @RequestParam(defaultValue = "20") Integer count
    ) {
        Long parsedChatroomId = chatroomId != null ? Long.parseLong(chatroomId) : null;
        Long parsedLastChatId = lastChatId != null ? Long.parseLong(lastChatId): null;

        GetChatMessagesUseCase.Param param = new GetChatMessagesUseCase.Param(parsedChatroomId, parsedLastChatId, count);
        GetChatMessagesUseCase.Result result = getChatMessagesUseCase.execute(param);

        ChatMessagesGetResDTO response = ChatMessagesGetResDTO.from(
                result.getMessages(),
                result.isHasNext(),
                result.getNextChatId()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<ChatroomsGetResDTO> getChatrooms(
            @CurrentMember Long currentMemberId,
            @RequestParam(required = false) String lastChatroomId,
            @RequestParam(defaultValue = "20") Integer count
    ) {
        log.info("채팅룸 리스트 조회");

        Long parsedLastChatroomId = lastChatroomId != null ? Long.parseLong(lastChatroomId) : null;

        GetChatroomsUseCase.Param param = new GetChatroomsUseCase.Param(currentMemberId, parsedLastChatroomId, count);
        GetChatroomsUseCase.Result result = getChatroomsUseCase.execute(param);

        ChatroomsGetResDTO response = ChatroomsGetResDTO.from(result, currentMemberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ChatroomOpenResDTO> openChatroom(
            @CurrentMember Long currentMemberId,
            @RequestParam(required = false) String chatroomId,
            @RequestParam(required = false) String otherMemberId,
            @RequestBody ChatroomOpenReqDTO reqDTO
    ){
        Long parsedChatroomId = chatroomId != null ? Long.parseLong(chatroomId) : null;
        Long parsedOtherMemberId = otherMemberId != null ? Long.parseLong(otherMemberId) : null;

        OpenChatroomUseCase.Param param = new OpenChatroomUseCase.Param(
                parsedChatroomId,
                currentMemberId, parsedOtherMemberId,
                reqDTO.postId(), reqDTO.postTitle(), reqDTO.helpCategoryIds()
        );

        OpenChatroomUseCase.Result result = openChatroomUseCase.execute(param);

        ChatroomOpenResDTO resDTO = ChatroomOpenResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }
}

