package com.lgcns.bebee.match.domain.entity.sync;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member_sync")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSync {
    @Id
    @Tsid
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 512)
    private String profileImageUrl;

    @Column
    private String addressRoad;

    @Column(nullable = false)
    private String legalDongCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberDisabilityCategorySync> disabilityCategories = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberHelpCategorySync> helpCategories = new ArrayList<>();

    public static MemberSync create(
            Long memberId,
            String nickname,
            Gender gender,
            Role role,
            LocalDate birthDate,
            Double latitude,
            Double longitude,
            String profileImageUrl,
            String addressRoad,
            String legalDongCode,
            List<MemberDisabilityCategorySync> disabilityCategories,
            List<MemberHelpCategorySync> helpCategories
    ) {
        MemberSync member = new MemberSync();
        member.id = memberId;
        member.nickname = nickname;
        member.gender = gender;
        member.role = role;
        member.birthDate = birthDate;
        member.latitude = latitude;
        member.longitude = longitude;
        member.profileImageUrl = profileImageUrl;
        member.addressRoad = addressRoad;
        member.legalDongCode = legalDongCode;
        member.createdAt = LocalDateTime.now();
        member.updatedAt = LocalDateTime.now();

        disabilityCategories.forEach(disabilityCategory -> disabilityCategory.assignToMember(member));
        helpCategories.forEach(helpCategory -> helpCategory.assignToMember(member));

        return member;
    }
}
