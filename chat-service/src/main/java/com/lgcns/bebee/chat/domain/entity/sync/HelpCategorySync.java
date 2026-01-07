package com.lgcns.bebee.chat.domain.entity.sync;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum HelpCategorySync {
    OUTING_COMPANION(1L, "외출동행"),
    VISIT_BATH(2L, "방문목욕"),
    VISIT_NURSING(3L, "방문간호"),
    HOUSEWORK_SUPPORT(4L, "가사지원"),
    MEAL_ASSISTANCE(5L, "식사도움"),
    LEARNING_SUPPORT(6L, "학습지원"),
    EMOTIONAL_SUPPORT(7L, "정서적지원"),
    OTHER_LIFE_SUPPORT(8L, "기타생활지원");

    private final Long id;
    private final String name;

    public static HelpCategorySync from(Long id) {
        return Arrays.stream(values())
                .filter(type -> type.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
    }
}
