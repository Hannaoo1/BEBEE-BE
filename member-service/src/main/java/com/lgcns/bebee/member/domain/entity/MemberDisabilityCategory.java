package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_disability_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDisabilityCategory extends BaseTimeEntity {

    @EmbeddedId
    private MemberDisabilityCategoryId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @MapsId("disabilityCategoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disability_category_id", nullable = false)
    private DisabilityCategory disabilityCategory;

    @Column(nullable = false, length = 1)
    private String level;

    @Column(nullable = false, length = 300)
    private String disabilityDescription;

    /**
     * MemberDisabilityCategory 생성 (정적 팩토리 메서드)
     * 
     * @param member             회원 엔티티
     * @param disabilityCategory 장애 카테고리 엔티티
     * @param level              장애 등급
     * @param description        장애 설명
     * @return 생성된 MemberDisabilityCategory
     */
    public static MemberDisabilityCategory create(Member member, DisabilityCategory disabilityCategory, 
                                                   String level, String description) {
        MemberDisabilityCategory memberDisabilityCategory = new MemberDisabilityCategory();
        memberDisabilityCategory.id = new MemberDisabilityCategoryId(
            member.getId(), 
            disabilityCategory.getDisabilityCategoryId()
        );
        memberDisabilityCategory.member = member;
        memberDisabilityCategory.disabilityCategory = disabilityCategory;
        memberDisabilityCategory.level = level;
        memberDisabilityCategory.disabilityDescription = description;
        return memberDisabilityCategory;
    }
}
