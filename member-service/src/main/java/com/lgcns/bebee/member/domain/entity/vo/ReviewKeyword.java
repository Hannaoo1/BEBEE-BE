package com.lgcns.bebee.member.domain.entity.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 리뷰 키워드 정보
 * match-service의 Keyword enum과 동기화 필요
 */
@Getter
@RequiredArgsConstructor
public enum ReviewKeyword {

    // 장애인 → 도우미 (긍정)
    DISABLED_TO_HELPER_PUNCTUAL(1, "시간 약속 잘 지켜요", true),
    DISABLED_TO_HELPER_QUICK_RESPONSE(2, "응답 속도 빨라요", true),
    DISABLED_TO_HELPER_KIND(3, "친절하고 매너 잘 지켜요", true),
    DISABLED_TO_HELPER_DETAILED_SUPPORT(4, "지원 방식이 세심하고 꼼꼼해요", true),
    DISABLED_TO_HELPER_DEVICE_SKILLED(5, "보조 기기 사용에 능숙해요", true),
    DISABLED_TO_HELPER_PROBLEM_SOLVING(6, "상황 대처 능력이 뛰어나요", true),
    DISABLED_TO_HELPER_RESPECTFUL(7, "선택권을 존중하고 의사를 먼저 물어봐요", true),
    DISABLED_TO_HELPER_APPROPRIATE_DISTANCE(8, "적절한 거리감을 유지해요", true),

    // 장애인 → 도우미 (부정)
    DISABLED_TO_HELPER_NO_UNDERSTANDING(9, "장애 특성에 대한 이해가 전혀 없어요", false),
    DISABLED_TO_HELPER_NO_RESPECT(10, "파트너에 대한 존중이 부족해요", false),
    DISABLED_TO_HELPER_UNCOMFORTABLE_TALK(11, "과도하게 사적인 대화를 시도해 부담스러워요", false),
    DISABLED_TO_HELPER_NOT_PUNCTUAL(12, "시간 약속을 잘 지키지 않아요", false),
    DISABLED_TO_HELPER_POOR_WORK(13, "부탁한 업무를 건성으로 처리해요", false),

    // 도우미 → 장애인 (긍정)
    HELPER_TO_DISABLED_PUNCTUAL(14, "시간 약속 잘 지켜요", true),
    HELPER_TO_DISABLED_QUICK_RESPONSE(15, "응답 속도 빨라요", true),
    HELPER_TO_DISABLED_KIND(16, "친절하고 매너를 잘 지켜요", true),
    HELPER_TO_DISABLED_CLEAR_COMMUNICATION(17, "의사표현이 명확해요", true),
    HELPER_TO_DISABLED_RESPECTFUL_JUDGMENT(18, "도우미의 판단을 존중해줘요", true),
    HELPER_TO_DISABLED_COMFORTABLE_ENVIRONMENT(19, "활동하기 편한 환경을 만들어줘요", true),

    // 도우미 → 장애인 (부정)
    HELPER_TO_DISABLED_NO_RESPECT(20, "파트너에 대한 존중이 부족해요", false),
    HELPER_TO_DISABLED_UNCOMFORTABLE_TALK(21, "과도하게 사적인 대화를 시도해 부담스러워요", false),
    HELPER_TO_DISABLED_NOT_PUNCTUAL(22, "시간 약속을 잘 지키지 않아요", false),
    HELPER_TO_DISABLED_UNREASONABLE_REQUEST(23, "무리한 요구 또는 역할 외의 부탁을 해요", false),
    HELPER_TO_DISABLED_UNCLEAR_REQUEST(24, "요청사항이 불명확하고 매번 바뀌어요", false);

    private final int id;
    private final String description;
    private final boolean isPositive;

    private static final Map<Integer, ReviewKeyword> BY_ID = Arrays.stream(values())
            .collect(Collectors.toMap(ReviewKeyword::getId, Function.identity()));

    public static ReviewKeyword fromId(int id) {
        ReviewKeyword keyword = BY_ID.get(id);
        if (keyword == null) {
            throw new IllegalArgumentException("Invalid keyword ID: " + id);
        }
        return keyword;
    }

    public static String getDescriptionById(int id) {
        ReviewKeyword keyword = BY_ID.get(id);
        return keyword != null ? keyword.getDescription() : "알 수 없는 키워드";
    }

    public static Boolean isPositiveById(int id) {
        ReviewKeyword keyword = BY_ID.get(id);
        return keyword != null ? keyword.isPositive() : null;
    }
}
