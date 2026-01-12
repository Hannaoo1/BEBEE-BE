package com.lgcns.bebee.member.presentation.dto.req;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSignUpReqDTO {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private LocalDate birthDate;
    private String gender;
    private String phoneNumber;
    private String role;
    private String addressRoad;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String districtCode;

    // HELPER용: 도움 유형 목록
    private java.util.List<String> helpTypes;

    // DISABLED용: 장애 유형 및 설명
    private String disabilityType;
    private String disabilityDescription;

    // 문서 관련 (Step 5에서 업로드 및 분석 완료)
    private String fileUrl;
    private String systemFlag;
}
