package com.lgcns.bebee.match.domain.entity.sync;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MemberHelpCategorySyncId implements Serializable {
    private Long memberId;
    private Long helpCategoryId;
}
