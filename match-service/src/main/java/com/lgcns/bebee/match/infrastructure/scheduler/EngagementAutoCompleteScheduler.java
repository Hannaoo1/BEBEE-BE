package com.lgcns.bebee.match.infrastructure.scheduler;

import com.lgcns.bebee.match.application.usecase.client.EventPublisher;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;  // ← enum import
import com.lgcns.bebee.match.domain.event.*;
import com.lgcns.bebee.match.domain.repository.EngagementRepository;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * 활동 자동 완료/미완료 처리 스케줄러
 *
 * 실행 시점: 매일 새벽 3시
 * 대상: 3일 전 활동 중 PENDING 상태
 *
 * 처리:
 * - 케이스 3 (도우미만 체크): 자동 완료 + 이벤트 3개 발행
 * - 케이스 4 (둘 다 미체크): 미완료 처리 + 이벤트 2개 발행
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class EngagementAutoCompleteScheduler {

    private final EngagementRepository engagementRepository;
    private final AgreementReader agreementReader;
    private final EventPublisher eventPublisher;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void autoProcessEngagements() {

        log.info("=== 활동 자동 처리 스케줄러 시작 ===");

        // 3일 전 날짜 계산
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);

        // PENDING 상태 활동들 조회
        List<Engagement> pendingEngagements = engagementRepository
                .findByActivityDateAndStatus(threeDaysAgo, EngagementStatus.PENDING);

        log.info("자동 처리 대상 활동 수: {}", pendingEngagements.size());

        int completedCount = 0;
        int incompletedCount = 0;

        // 각 활동 처리
        for (Engagement engagement : pendingEngagements) {
            Agreement agreement = agreementReader.getById(engagement.getAgreementId());

            if (engagement.isHelperCheck() && !engagement.isDisabledCheck()) {

                // 케이스 3: 도우미만 체크 → 자동 완료
                processAutoComplete(engagement, agreement);
                completedCount++;

            } else if (!engagement.isHelperCheck() && !engagement.isDisabledCheck()) {

                // 케이스 4: 둘 다 미체크 → 미완료 처리
                processIncomplete(engagement, agreement);
                incompletedCount++;
            }
        }

        log.info("=== 활동 자동 처리 스케줄러 종료: 완료 {}건, 미완료 {}건 ===",
                completedCount, incompletedCount);
    }


    // 케이스 3: 도우미만 체크 → 자동 완료 처리
    private void processAutoComplete(Engagement engagement, Agreement agreement) {
        log.info("활동 자동 완료 처리: engagementId={}", engagement.getEngagementId());

        engagement.complete();

        // 정산 이벤트 발행
        eventPublisher.publish(new HoneySettlementEvent(
                agreement.getId(),
                engagement.getEngagementId(),
                agreement.getHelperId(),
                agreement.getDisabledId(),
                agreement.getUnitHoney(),
                engagement.getActivityDate()
        ));

        // 완료 알림 이벤트 발행 (장애인에게만)
        eventPublisher.publish(new ActivityCompletedEvent(
                engagement.getEngagementId(),
                agreement.getId(),
                null,
                agreement.getDisabledId(),
                engagement.getActivityDate(),
                true
        ));

        // 리뷰 요청 이벤트 발행
        eventPublisher.publish(new ReviewRequestEvent(
                agreement.getId(),
                List.of(engagement.getEngagementId()),
                agreement.getHelperId(),
                agreement.getDisabledId()
        ));
    }

    // 케이스 4: 둘 다 미체크 → 미완료 처리
    private void processIncomplete(Engagement engagement, Agreement agreement) {
        log.info("활동 미완료 처리: engagementId={}", engagement.getEngagementId());

        // 미완료 처리
        engagement.incompleted();

        // 환급 이벤트 발행
        eventPublisher.publish(new HoneyRefundEvent(
                agreement.getId(),
                engagement.getEngagementId(),
                agreement.getDisabledId(),
                agreement.getUnitHoney(),
                engagement.getActivityDate()
        ));

        // 미완료 알림 이벤트 발행 (둘 다)
        eventPublisher.publish(new ActivityIncompletedEvent(
                engagement.getEngagementId(),
                agreement.getId(),
                agreement.getHelperId(),
                agreement.getDisabledId(),
                engagement.getActivityDate()
        ));
    }
}