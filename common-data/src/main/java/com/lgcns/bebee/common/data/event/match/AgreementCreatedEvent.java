package com.lgcns.bebee.common.data.event.match;

import com.lgcns.bebee.common.data.dto.ScheduleDTO;
import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class AgreementCreatedEvent implements DomainEvent {
    private final Long chatroomId;

    private final Long agreementId;
    private final Long disabledId;
    private final Long helperId;

    private final String type;
    private final Boolean isVolunteer;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private final List<ScheduleDTO> schedules;
    private final String region;
    private final Integer unitHoney;
    private final Integer totalHoney;

    private final List<Long> helpCategoryIds;

    private final LocalDateTime createdAt;
}
