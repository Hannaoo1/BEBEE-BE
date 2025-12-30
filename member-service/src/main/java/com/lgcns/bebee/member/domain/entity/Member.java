package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import com.lgcns.bebee.member.core.exception.MemberErrors;
import com.lgcns.bebee.member.domain.entity.vo.Gender;
import com.lgcns.bebee.member.domain.entity.vo.MemberStatus;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 10, unique = true)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender = Gender.NONE;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status;

    @Column(length = 255)
    private String profileImageUrl;

    @Column(length = 255)
    private String addressRoad;

    @Column(length = 255)
    private String introduction;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(nullable = false, length = 10)
    private String districtCode;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal sweetness = BigDecimal.valueOf(40.00);

    public static Member create(String email,
            String encodedPassword,
            String name,
            String nickname,
            LocalDate birthDate,
            String gender,
            String phoneNumber,
            String role,
            String addressRoad,
            BigDecimal latitude,
            BigDecimal longitude,
            String districtCode) {
        Member member = new Member();
        member.email = email;
        member.password = encodedPassword;
        member.name = name;
        member.nickname = nickname;
        member.birthDate = birthDate;
        member.gender = Gender.valueOf(gender);
        member.phoneNumber = phoneNumber;
        member.role = Role.from(role);
        member.status = MemberStatus.PENDING_APPROVAL;
        member.addressRoad = addressRoad;
        member.latitude = latitude;
        member.longitude = longitude;
        member.districtCode = districtCode;
        // profileImageUrl, introduction, sweetness 는 기본값 사용
        return member;
    }

    public void validateLoginAvailable() {
        switch (this.status) {
            case REJECTED -> throw MemberErrors.MEMBER_STATUS_REJECTED.toException();
            case WITHDRAWN, WITHDRAW_APPROVAL -> throw MemberErrors.MEMBER_STATUS_WITHDRAWN.toException();
            case ACTIVE, PENDING_APPROVAL -> {
                /* 정상 */ }
        }
    }

    /**
     * 회원을 활성화 상태로 변경합니다.
     */
    public void activate() {
        this.status = MemberStatus.ACTIVE;
    }

    /**
     * 회원을 거절 상태로 변경합니다.
     */
    public void reject() {
        this.status = MemberStatus.REJECTED;
    }
}
