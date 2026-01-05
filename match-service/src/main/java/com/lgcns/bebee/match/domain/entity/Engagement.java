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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Engagement extends BaseTimeEntity {
    @Id
    @Tsid
    private Long engagementId;

    @Column(nullable = false)
    private Long agreementId;

    @Enumerated(EnumType.STRING)
    private EngagementType type;

    @Enumerated(EnumType.STRING)
    private EngagementStatus status;

    @Column(nullable = false)
    private Boolean isDisabledCheck;

    @Column(nullable = false)
    private Boolean isHelperCheck;

    @Column(nullable = false)
    private LocalDate activityDate;

    @Column(nullable = false)
    private Long completedCount;

    public static Engagement create(Long agreementId, EngagementType type, LocalDate activityDate) {
        Engagement engagement = new Engagement();
        engagement.agreementId = agreementId;
        engagement.type = type;
        engagement.activityDate = activityDate;
        engagement.status = EngagementStatus.PENDING;
        engagement.isDisabledCheck = false;
        engagement.isHelperCheck = false;
        engagement.completedCount = 0L;
        return engagement;
    }

    public void setDisabledCheck() {
        this.isDisabledCheck = true;
    }

    public void setHelperCheck() {
        this.isHelperCheck = true;
    }

    // 완료 처리
    public void complete() {
        this.status = EngagementStatus.COMPLETED;
        this.completedCount += 1;
    }

    // 미완료 처리
    public void incompleted() {
        this.status = EngagementStatus.INCOMPLETED;
    }

    public Boolean isHelperCheck() {
        return isHelperCheck;
    }

    public Boolean isDisabledCheck() {
        return isDisabledCheck;
    }

    public boolean isLastActivity(Agreement agreement) {
        if (this.type == EngagementType.DAY) {

            // 하루 도움은 항상 마지막 활동
            return true;
        }

        return true;
    }
}
