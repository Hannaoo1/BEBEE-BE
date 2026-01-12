package com.lgcns.bebee.match.domain.entity.sync;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member_help_category_sync")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberHelpCategorySync extends BaseTimeEntity {
    @EmbeddedId
    private MemberHelpCategorySyncId id;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id", nullable = false)
    private MemberSync memberSync;

    public static MemberHelpCategorySync create(Long helpCategoryId) {
        MemberHelpCategorySync category = new MemberHelpCategorySync();
        category.id = new MemberHelpCategorySyncId(null, helpCategoryId);
        return category;
    }
}
