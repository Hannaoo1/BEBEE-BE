package com.lgcns.bebee.match.infrastructure.scheduler;

import com.lgcns.bebee.match.application.usecase.client.EventPublisher;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.vo.EngagementStatus;  // ← enum import
import com.lgcns.bebee.match.domain.event.WarningNotificationEvent;
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
 * 활동 완료 경고 스케줄러
 * 실행 시점: 매일 새벽 3시
 * 대상: 2일 전 활동 중 둘 다 완료 체크 안 한 PENDING 상태
 * 처리: WarningNotificationEvent 발행 (1일 뒤 미완료 처리 경고)
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class EngagementWarningScheduler {

    private final EngagementRepository engagementRepository;
    private final AgreementReader agreementReader;
    private final EventPublisher eventPublisher;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void sendWarningNotifications() {
        log.info("=== 활동 완료 경고 스케줄러 시작 ===");

        // 2일 전 날짜 계산
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

        // PENDING 상태 활동들 조회
        List<Engagement> warningTargets = engagementRepository
                .findByActivityDateAndStatus(twoDaysAgo, EngagementStatus.PENDING);

        log.info("경고 대상 활동 수: {}", warningTargets.size());

        int warningCount = 0;

        // 활동 처리
        for (Engagement engagement : warningTargets) {

            // 둘 다 체크 안 한 경우만 (케이스 4)
            if (!engagement.isHelperCheck() && !engagement.isDisabledCheck()) {

                Agreement agreement = agreementReader.getById(engagement.getAgreementId());

                // 경고 알림 이벤트 발행
                eventPublisher.publish(new WarningNotificationEvent(
                        engagement.getEngagementId(),
                        agreement.getId(),
                        agreement.getHelperId(),
                        agreement.getDisabledId(),
                        engagement.getActivityDate()
                ));

                warningCount++;
            }
        }

        log.info("=== 활동 완료 경고 스케줄러 종료: {}건 경고 발송 ===", warningCount);
    }
}