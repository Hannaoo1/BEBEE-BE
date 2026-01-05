package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseTimeEntity {
    @Id
    @Tsid
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private Long applicantId;

    @Column(nullable = false)
    private Boolean isVolunteer;

    public static Application create(
            Long memberId,
            Post post,
            Boolean isVolunteer
    ) {

        Application application = new Application();
        application.applicantId =  memberId;
        application.post = post;
        application.isVolunteer = isVolunteer;

        return application;
    }
}
