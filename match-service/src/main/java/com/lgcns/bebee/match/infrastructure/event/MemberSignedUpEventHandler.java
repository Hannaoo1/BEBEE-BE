package com.lgcns.bebee.match.infrastructure.event;

import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.member.MemberSignedUpEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberSignedUpEventHandler implements EventHandler<MemberSignedUpEvent> {

    @Override
    public Class<MemberSignedUpEvent> getEventClass() {
        return MemberSignedUpEvent.class;
    }

    @Override
    public void handle(MemberSignedUpEvent event) {

    }
}
