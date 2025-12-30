package com.lgcns.bebee.common.util;

import java.time.LocalDate;
import java.time.Period;

public class AgeGroupCalculator {

    /**
     * 출생일을 기준으로 현재 연령대 계산 (만 나이 기준)
     * @param birth 출생일
     * @return 연령대
     */
    public static int calculateAgeGroup(LocalDate birth) {
        if (birth == null) {
            return 0;
        }

        int age = Period.between(birth, LocalDate.now()).getYears();
        return (age / 10) * 10;
    }
}
