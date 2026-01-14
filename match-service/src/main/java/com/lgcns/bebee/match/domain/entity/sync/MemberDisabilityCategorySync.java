package com.lgcns.bebee.match.domain.entity.sync;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_disability_category_sync")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDisabilityCategorySync extends BaseTimeEntity {
    @EmbeddedId
    private MemberDisabilityCategorySyncId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id", nullable = false)
    private MemberSync member;

    public static MemberDisabilityCategorySync create(Long disabilityCategoryId) {
        MemberDisabilityCategorySync category = new MemberDisabilityCategorySync();
        category.id = new MemberDisabilityCategorySyncId(null, disabilityCategoryId);
        return category;
    }

    protected void assignToMember(MemberSync member) {
        this.member = member;
    }
}
