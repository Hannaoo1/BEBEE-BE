package com.lgcns.bebee.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

/**
 * 문자열 유사도 계산 유틸리티
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimilarityUtil {

    private static final JaroWinklerSimilarity JARO_WINKLER = new JaroWinklerSimilarity();

    // 한글 유니코드 정보
    private static final char HANGEUL_BASE = 0xAC00;
    private static final char HANGEUL_LIMIT = 0xD7A3;

    private static final char[] CHOSUNG = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };
    private static final char[] JUNGSUNG = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    };
    private static final char[] JONGSUNG = {
            '\0', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ',
            'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    /**
     * 두 문자열의 유사도를 계산합니다. (한글 자소 분리 지원)
     *
     * @param left  비교할 문자열 1
     * @param right 비교할 문자열 2
     * @return 0.0 ~ 1.0 사이의 값
     */
    public static double calculateSimilarity(String left, String right) {
        if (left == null || right == null) {
            return 0.0;
        }

        String leftClean = decomposeHangeul(left.trim().toLowerCase());
        String rightClean = decomposeHangeul(right.trim().toLowerCase());

        if (leftClean.isEmpty() && rightClean.isEmpty()) {
            return 1.0;
        }

        if (leftClean.isEmpty() || rightClean.isEmpty()) {
            return 0.0;
        }

        return JARO_WINKLER.apply(leftClean, rightClean);
    }

    /**
     * 한글 문자열을 초성, 중성, 종성 단위로 분리합니다.
     */
    private static String decomposeHangeul(String input) {
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c >= HANGEUL_BASE && c <= HANGEUL_LIMIT) {
                int baseIndex = c - HANGEUL_BASE;
                int choIndex = baseIndex / (21 * 28);
                int jungIndex = (baseIndex / 28) % 21;
                int jongIndex = baseIndex % 28;

                sb.append(CHOSUNG[choIndex]);
                sb.append(JUNGSUNG[jungIndex]);
                if (jongIndex > 0) {
                    sb.append(JONGSUNG[jongIndex]);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
