package com.lgcns.bebee.match.domain.entity.sync;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 뱃지 동기화 엔티티
 * member-service의 Badge를 match-service에서 조회하기 위한 복제본
 */
@Entity
@Table(name = "badge_sync",
        uniqueConstraints = @UniqueConstraint(columnNames = {"helper_id", "disability_category_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BadgeSync {

    @Id
    @Tsid
    @Column(name = "badge_sync_id")
    private Long id;

    @Column(name = "helper_id", nullable = false)
    private Long helperId;

    @Column(name = "disability_category_id", nullable = false)
    private Long disabilityCategoryId;

    @Column(name = "completion_count", nullable = false)
    private Integer completionCount;

    @Column(name = "badge_code", length = 20)
    private String badgeCode;

    // 생성 메서드
    public static BadgeSync create(Long helperId, Long disabilityCategoryId) {
        BadgeSync badge = new BadgeSync();
        badge.helperId = helperId;
        badge.disabilityCategoryId = disabilityCategoryId;
        badge.completionCount = 0;
        badge.badgeCode = null;
        return badge;
    }

    // 도메인 메서드: 뱃지 정보 업데이트
    public void updateBadge(Integer completionCount, String badgeCode) {
        this.completionCount = completionCount;
        this.badgeCode = badgeCode;
    }
}
