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
    @Tsid @Column(name = "engagement_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private EngagementType type;

    @Enumerated(EnumType.STRING)
    private EngagementStatus status;

    @Column(nullable = false)
    private Boolean isDisabledCheck;

    @Column(nullable = false)
    private Boolean isHelperCheck;

    public static Engagement create(Match match, EngagementType type, LocalDate date) {
        Engagement engagement = new Engagement();
        engagement.match = match;
        engagement.type = type;
        engagement.date = date;
//        engagement.status = EngagementStatus.PENDING;
        engagement.isDisabledCheck = false;
        engagement.isHelperCheck = false;
        return engagement;
    }

    public void checkHelper(){
        this.isHelperCheck = true;
    }

    public void checkDisabled(){
        this.isDisabledCheck = true;
    }

    public void complete(){
        if(this.isDisabledCheck) {
            this.status = EngagementStatus.COMPLETED;
        }
    }
}
