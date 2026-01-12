package com.lgcns.bebee.member.domain.entity.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DisabilityCategoryType {
    PHYSICAL(1L, "지체장애"),
    VISUAL(2L, "시각장애"),
    HEARING(3L, "청각장애"),
    DEVELOPMENTAL(4L, "발달장애"),
    INTERNAL_ORGAN(5L, "내부기관장애"),
    ETC(6L, "기타장애")
    ;

    private final Long id;
    private final String name;

    public static String getNameById(Long id) {
        return from(id).getName();
    }

    public static DisabilityCategoryType from(Long id) {
        return Arrays.stream(values())
                .filter(type -> type.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
    }
}
