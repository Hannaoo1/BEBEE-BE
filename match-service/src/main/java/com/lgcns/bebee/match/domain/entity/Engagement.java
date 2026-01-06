package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Engagement extends BaseTimeEntity {
    @Id
    @Tsid
    private Long engagementId;

    @Column(nullable = false)
    private Long agreementId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EngagementType type;

    @Column(nullable = false)
    private LocalDate activityDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EngagementStatus status = EngagementStatus.PENDING;

    @Column(nullable = false)
    private Boolean isDisabledCheck = false;

    @Column(nullable = false)
    private Boolean isHelperCheck = false;

    @Column(nullable = false)
    private Long completedCount = 0L;

    // 활동 완료 체크 (장애인)
    public void checkByDisabled() {
        this.isDisabledCheck = true;
        completeIfBothChecked();
    }

    // 활동 완료 체크 (도우미)
    public void checkByHelper() {
        this.isHelperCheck = true;
        completeIfBothChecked();
    }

    // 양쪽 모두 체크 시 완료 처리
    private void completeIfBothChecked() {
        if (this.isHelperCheck && this.isDisabledCheck) {
            this.status = EngagementStatus.COMPLETED;
            this.completedCount++;
        }
    }

    // 마지막 활동인지 확인
    public boolean isLastActivity(Agreement agreement) {
        if (this.type == EngagementType.DAY) {
            // 하루 도움은 항상 마지막 활동
            return true;
        }

        return true;
    }
}
