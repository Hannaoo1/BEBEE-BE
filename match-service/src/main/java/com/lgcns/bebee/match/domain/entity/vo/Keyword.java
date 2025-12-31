package com.lgcns.bebee.match.domain.entity.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Keyword {

    // 장애인 → 도우미 (긍정)
    DISABLED_TO_HELPER_PUNCTUAL(1, "시간 약속 잘 지켜요",
            ReviewDirection.DISABLED_TO_HELPER, true),
    DISABLED_TO_HELPER_QUICK_RESPONSE(2, "응답 속도 빨라요",
            ReviewDirection.DISABLED_TO_HELPER, true),
    DISABLED_TO_HELPER_KIND(3, "친절하고 매너 잘 지켜요",
            ReviewDirection.DISABLED_TO_HELPER, true),
    DISABLED_TO_HELPER_DETAILED_SUPPORT(4, "지원 방식이 세심하고 꼼꼼해요",
            ReviewDirection.DISABLED_TO_HELPER, true),
    DISABLED_TO_HELPER_DEVICE_SKILLED(5, "보조 기기 사용에 능숙해요",
            ReviewDirection.DISABLED_TO_HELPER, true),
    DISABLED_TO_HELPER_PROBLEM_SOLVING(6, "상황 대처 능력이 뛰어나요",
            ReviewDirection.DISABLED_TO_HELPER, true),
    DISABLED_TO_HELPER_RESPECTFUL(7, "선택권을 존중하고 의사를 먼저 물어봐요",
            ReviewDirection.DISABLED_TO_HELPER, true),
    DISABLED_TO_HELPER_APPROPRIATE_DISTANCE(8, "적절한 거리감을 유지해요",
            ReviewDirection.DISABLED_TO_HELPER, true),

    // 장애인 → 도우미 (부정)
    DISABLED_TO_HELPER_NO_UNDERSTANDING(9, "장애 특성에 대한 이해가 전혀 없어요",
            ReviewDirection.DISABLED_TO_HELPER, false),
    DISABLED_TO_HELPER_NO_RESPECT(10, "파트너에 대한 존중이 부족해요",
            ReviewDirection.DISABLED_TO_HELPER, false),
    DISABLED_TO_HELPER_UNCOMFORTABLE_TALK(11, "과도하게 사적인 대화를 시도해 부담스러워요",
            ReviewDirection.DISABLED_TO_HELPER, false),
    DISABLED_TO_HELPER_NOT_PUNCTUAL(12, "시간 약속을 잘 지키지 않아요",
            ReviewDirection.DISABLED_TO_HELPER, false),
    DISABLED_TO_HELPER_POOR_WORK(13, "부탁한 업무를 건성으로 처리해요",
            ReviewDirection.DISABLED_TO_HELPER, false),

    // 도우미 → 장애인 (긍정)
    HELPER_TO_DISABLED_PUNCTUAL(14, "시간 약속 잘 지켜요",
            ReviewDirection.HELPER_TO_DISABLED, true),
    HELPER_TO_DISABLED_QUICK_RESPONSE(15, "응답 속도 빨라요",
            ReviewDirection.HELPER_TO_DISABLED, true),
    HELPER_TO_DISABLED_KIND(16, "친절하고 매너를 잘 지켜요",
            ReviewDirection.HELPER_TO_DISABLED, true),
    HELPER_TO_DISABLED_CLEAR_COMMUNICATION(17, "의사표현이 명확해요",
            ReviewDirection.HELPER_TO_DISABLED, true),
    HELPER_TO_DISABLED_RESPECTFUL_JUDGMENT(18, "도우미의 판단을 존중해줘요",
            ReviewDirection.HELPER_TO_DISABLED, true),
    HELPER_TO_DISABLED_COMFORTABLE_ENVIRONMENT(19, "활동하기 편한 환경을 만들어줘요",
            ReviewDirection.HELPER_TO_DISABLED, true),

    // 도우미 → 장애인 (부정)
    HELPER_TO_DISABLED_NO_RESPECT(20, "파트너에 대한 존중이 부족해요",
            ReviewDirection.HELPER_TO_DISABLED, false),
    HELPER_TO_DISABLED_UNCOMFORTABLE_TALK(21, "과도하게 사적인 대화를 시도해 부담스러워요",
            ReviewDirection.HELPER_TO_DISABLED, false),
    HELPER_TO_DISABLED_NOT_PUNCTUAL(22, "시간 약속을 잘 지키지 않아요",
            ReviewDirection.HELPER_TO_DISABLED, false),
    HELPER_TO_DISABLED_UNREASONABLE_REQUEST(23, "무리한 요구 또는 역할 외의 부탁을 해요",
            ReviewDirection.HELPER_TO_DISABLED, false),
    HELPER_TO_DISABLED_UNCLEAR_REQUEST(24, "요청사항이 불명확하고 매번 바뀌어요",
            ReviewDirection.HELPER_TO_DISABLED, false);

    private final int id;
    private final String description;
    private final ReviewDirection direction;
    private final boolean isPositive;

    // id를 통해 키워드 검증
    public static Keyword fromId(int id) {
        return Arrays.stream(values())
                .filter(keyword -> keyword.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid keyword ID: " + id));
    }

    // 방향에 따른 키워드 목록 조회
    public static List<Keyword> getByDirection(ReviewDirection direction) {
        return Arrays.stream(values())
                .filter(keyword -> keyword.direction == direction)
                .collect(Collectors.toList());
    }

    // 키워드가 해당 방향에 속하는지 확인
    public boolean belongsToDirection(ReviewDirection direction) {
        return this.direction == direction;
    }
    
}
