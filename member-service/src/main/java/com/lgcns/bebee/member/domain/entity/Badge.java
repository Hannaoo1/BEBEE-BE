package com.lgcns.bebee.member.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Badge extends BaseTimeEntity {

    @Id
    @Tsid
    @Column(name = "badge_id")
    private Long id;

    @Column(nullable = false)
    private Long helperId;

    // 활동 완료 횟수
    @Column(nullable = false)
    private Integer completionCount = 0;

    // 뱃지 조건 -> null, LEVEL_1, LEVEL_2
    @Column(name = "badge_code", length = 20)
    private String badgeCode;

    @Column(name = "disability_category_id", nullable = false)
    private Long disabilityCategoryId;

    // 뱃지 생성
    public static Badge create(
            Long helperId,
            Long disabilityCategoryId
    ) {
        Badge badge = new Badge();
        badge.helperId = helperId;
        badge.disabilityCategoryId = disabilityCategoryId;
        badge.completionCount = 0;
        badge.badgeCode = null;
        return badge;
    }

    public void incrementCompletionCount() {
        this.completionCount++;
        this.updateBadgeCode();
    }

    // 활동 완료 횟수에 따른 뱃지 레벨 확정
    private void updateBadgeCode() {
        if (this.completionCount >= 10) {
            this.badgeCode = "LEVEL_2";
        } else if (this.completionCount >= 5) {
            this.badgeCode = "LEVEL_1";
        } else {
            this.badgeCode = null;
        }
    }
}
