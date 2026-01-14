package com.lgcns.bebee.chat.infrastructure.event;

import com.lgcns.bebee.chat.application.CreateMemberUseCase;
import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.member.MemberSignedUpEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberSignedUpEventHandler implements EventHandler<MemberSignedUpEvent> {
    private final CreateMemberUseCase createMemberUseCase;

    @Override
    public Class<MemberSignedUpEvent> getEventClass() {
        return MemberSignedUpEvent.class;
    }

    @Override
    public void handle(MemberSignedUpEvent event) {
        CreateMemberUseCase.Param param = new CreateMemberUseCase.Param(
                event.getMemberId(),
                event.getNickname(),
                event.getProfileImageUrl());

        createMemberUseCase.execute(param);
    }
}
