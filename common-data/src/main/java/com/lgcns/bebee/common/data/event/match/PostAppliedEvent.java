package com.lgcns.bebee.common.data.event.match;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostAppliedEvent implements DomainEvent {
    private final Long disabledId;
    private final Long helperId;
    private final Long applicationId;
    private final Long postId;
    private final String postTitle;
}
