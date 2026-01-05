package com.lgcns.bebee.match.domain.entity.sync;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class MemberDisabilityCategorySyncId implements Serializable {
    private Long memberId;
    private Long disabilityCategoryId;

    public MemberDisabilityCategorySyncId(Long memberId, Long disabilityCategoryId) {
        this.memberId = memberId;
        this.disabilityCategoryId = disabilityCategoryId;
    }
}