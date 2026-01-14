package com.lgcns.bebee.common.data.event.member;

import com.lgcns.bebee.common.data.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MemberSignedUpEvent implements DomainEvent {
    private final Long memberId;
    private final String nickname;
    private final String gender;           // MALE, FEMALE, NONE
    private final String role;             // ADMIN, DISABLED, HELPER
    private final LocalDate birthDate;
    private final Double latitude;
    private final Double longitude;
    private final String profileImageUrl;
    private final String addressRoad;
    private final String legalDongCode;
    private final List<Long> disabilityCategoryIds;  // 장애 유형 ID 목록
    private final List<Long> helpCategoryIds;        // 도움 유형 ID 목록
}
