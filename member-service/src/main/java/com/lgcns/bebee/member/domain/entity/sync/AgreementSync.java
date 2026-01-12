package com.lgcns.bebee.member.domain.entity.sync;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "agreement_sync")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgreementSync {

    @Id
    @Column(name = "agreement_id")
    private Long id;

    @Column(name = "helper_id", nullable = false)
    private Long helperId;

    @Column(name = "disabled_id", nullable = false)
    private Long disabledId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
