package com.lgcns.bebee.chat.presentation;

import com.lgcns.bebee.chat.application.GetChatMessagesUseCase;
import com.lgcns.bebee.chat.application.GetChatroomsUseCase;
import com.lgcns.bebee.chat.application.OpenChatroomUseCase;
import com.lgcns.bebee.chat.presentation.dto.req.ChatroomOpenReqDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatMessagesGetResDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatroomOpenResDTO;
import com.lgcns.bebee.chat.presentation.dto.res.ChatroomsGetResDTO;
import com.lgcns.bebee.chat.presentation.swagger.ChatroomSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam String currentMemberId,
            @RequestParam(required = false) String lastChatroomId,
            @RequestParam(defaultValue = "20") Integer count
    ) {
        Long parsedCurrentMemberId = currentMemberId != null ? Long.parseLong(currentMemberId) : null;
        Long parsedLastChatroomId = lastChatroomId != null ? Long.parseLong(lastChatroomId) : null;

        GetChatroomsUseCase.Param param = new GetChatroomsUseCase.Param(parsedCurrentMemberId, parsedLastChatroomId, count);
        GetChatroomsUseCase.Result result = getChatroomsUseCase.execute(param);

        ChatroomsGetResDTO response = ChatroomsGetResDTO.from(result, parsedCurrentMemberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ChatroomOpenResDTO> openChatroom(
            @RequestParam(required = false) String chatroomId,
            @RequestParam String currentMemberId,
            @RequestParam(required = false) String otherMemberId,
            @RequestBody ChatroomOpenReqDTO reqDTO
    ){
        Long parsedChatroomId = chatroomId != null ? Long.parseLong(chatroomId) : null;
        Long parsedCurrentMemberId = currentMemberId != null ? Long.parseLong(currentMemberId) : null;
        Long parsedOtherMemberId = otherMemberId != null ? Long.parseLong(otherMemberId) : null;

        OpenChatroomUseCase.Param param = new OpenChatroomUseCase.Param(
                parsedChatroomId,
                parsedCurrentMemberId, parsedOtherMemberId,
                reqDTO.postId(), reqDTO.postTitle(), reqDTO.helpCategoryIds()
        );

        OpenChatroomUseCase.Result result = openChatroomUseCase.execute(param);

        ChatroomOpenResDTO resDTO = ChatroomOpenResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }
}

