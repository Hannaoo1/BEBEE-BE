package com.lgcns.bebee.match.infrastructure.event;

import com.lgcns.bebee.common.data.event.EventHandler;
import com.lgcns.bebee.common.data.event.member.MemberSignedUpEvent;
import com.lgcns.bebee.match.application.usecase.CreateMemberUseCase;
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
                event.getGender(),
                event.getRole(),
                event.getBirthDate(),
                event.getLatitude(),
                event.getLongitude(),
                event.getProfileImageUrl(),
                event.getAddressRoad(),
                event.getLegalDongCode(),
                event.getDisabilityCategoryIds(),
                event.getHelpCategoryIds()
        );

        createMemberUseCase.execute(param);
    }
}
