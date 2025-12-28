package com.lgcns.bebee.match.domain.entity;

import com.lgcns.bebee.common.domain.BaseTimeEntity;
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

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Long chatRoomId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false, unique = true)
    private Agreement agreement;

    public boolean isParticipant(Long memberId) {
        return this.helperId.equals(memberId) || this.disabledId.equals(memberId);
    }

    public Long getAgreementId() {
        return agreement != null ? agreement.getId() : null;
    }

    public static Match create(
            Long helperId,
            Long disabledId,
            Long postId,
            String title,
            Long chatRoomId,
            Agreement agreement
    ) {
        Match match = new Match();
        match.helperId = helperId;
        match.disabledId = disabledId;
        match.postId = postId;
        match.title = title;
        match.chatRoomId = chatRoomId;
        match.agreement = agreement;

        return match;
    }
}
