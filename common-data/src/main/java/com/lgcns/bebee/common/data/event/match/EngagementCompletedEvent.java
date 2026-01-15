package com.lgcns.bebee.common.data.event.match;
import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class EngagementCompletedEvent implements DomainEvent {
    private final Long engagementId;
    private final Long agreementId;
    private final Long matchId;
    private final Long helperId;
    private final Long disabledId;
    private final LocalDate engagementDate;
}
