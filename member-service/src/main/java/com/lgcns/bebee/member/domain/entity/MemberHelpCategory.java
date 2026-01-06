package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member_help_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberHelpCategory extends BaseTimeEntity {

    @EmbeddedId
    private MemberHelpCategoryId id;

    @MapsId("helpCategoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "help_category_id", nullable = false)
    private HelpCategory helpCategory;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * MemberHelpCategory 생성 (정적 팩토리 메서드)
     * 
     * @param member       회원 엔티티
     * @param helpCategory 도움 카테고리 엔티티
     * @return 생성된 MemberHelpCategory
     */
    public static MemberHelpCategory create(Member member, HelpCategory helpCategory) {
        MemberHelpCategory memberHelpCategory = new MemberHelpCategory();
        memberHelpCategory.id = new MemberHelpCategoryId(member.getId(), helpCategory.getHelpCategoryId());
        memberHelpCategory.member = member;
        memberHelpCategory.helpCategory = helpCategory;
        return memberHelpCategory;
    }
}
