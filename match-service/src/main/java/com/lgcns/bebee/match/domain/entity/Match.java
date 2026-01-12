package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.data.domain.BaseTimeEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`match`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseTimeEntity {
    @Id
    @Tsid
    private Long matchId;

    @Column(nullable = false)
    private Long helperId;

    @Column(nullable = false)
    private Long disabledId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Long chatRoomId;

    @Column(nullable = false)
    private String imageUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false, unique = true)
    private Agreement agreement;

    @OneToOne(mappedBy = "match")
    private Review helperReview;  // 도우미가 작성한 리뷰

    @OneToOne(mappedBy = "match")
    private Review disabledReview;

    public Long getAgreementId() {
        return agreement != null ? agreement.getId() : null;
    }

    public static Match create(
            Long helperId,
            Long disabledId,
            String title,
            String imageUrl,
            Long chatRoomId,
            Agreement agreement
    ) {
        Match match = new Match();
        match.helperId = helperId;
        match.disabledId = disabledId;
        match.title = title;
        match.imageUrl = imageUrl;
        match.chatRoomId = chatRoomId;
        match.agreement = agreement;

        return match;
    }
}
