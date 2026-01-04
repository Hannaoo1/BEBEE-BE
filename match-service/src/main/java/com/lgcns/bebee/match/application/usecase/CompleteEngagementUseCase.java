package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.match.application.usecase.client.EventPublisher;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.event.ActivityCompletedEvent;
import com.lgcns.bebee.match.domain.event.HoneySettlementEvent;
import com.lgcns.bebee.match.domain.event.ReviewRequestEvent;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import com.lgcns.bebee.match.domain.service.EngagementReader;
import com.lgcns.bebee.match.presentation.dto.req.EngagementCompleteReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.EngagementCompleteResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompleteEngagementUseCase {

    private final EngagementReader engagementReader;
    private final AgreementReader agreementReader;
    private final EventPublisher eventPublisher;

    @Transactional
    public EngagementCompleteResDTO execute(
            Long engagementId,
            String memberId,
            EngagementCompleteReqDTO request
    ) {

        Engagement engagement = engagementReader.getById(engagementId);

        // Agreement 조회 (이벤트)
        Agreement agreement = agreementReader.getById(engagement.getAgreementId());

        // 완료 체크 처리
        String userType = request.getUserType();
        if ("HELPER".equals(userType)) {
            engagement.setHelperCheck();
        } else if ("DISABLED".equals(userType)) {
            engagement.setDisabledCheck();
        }

        // 케이스 별 완료 여부 확인 및 처리
        if (engagement.isHelperCheck() && engagement.isDisabledCheck()) {

            // 케이스 1: 둘 다 완료 (즉시 완료)
            engagement.complete();
            publishCompletionEvents(engagement, agreement, true, true);

        } else if (engagement.isDisabledCheck()) {

            // 케이스 2: 장애인만 완료 (즉시 완료)
            engagement.complete();
            publishCompletionEvents(engagement, agreement, true, false);
        }

            // 케이스 3: 도우미만 완료 (PENDING 유지, 3일 후 스케줄러 처리)
            // 케이스 4: 둘 다 클릭 x (UseCase 실행 안됨, 3일 후 스케줄러 처리)

        String status = engagement.getStatus().name();
        Boolean isLastActivity = true;  // DAY는 항상 true

        return EngagementCompleteResDTO.of(status, isLastActivity);
    }

    private void publishCompletionEvents(
            Engagement engagement,
            Agreement agreement,
            boolean notifyHelper,   // 도우미 알림 여부
            boolean notifyDisabled  // 장애인 알림 여부
    ) {

        // 정산 이벤트 (결제 서비스)
        eventPublisher.publish(new HoneySettlementEvent(
                agreement.getId(),
                engagement.getEngagementId(),
                agreement.getHelperId(),
                agreement.getDisabledId(),
                agreement.getUnitHoney(),
                engagement.getActivityDate()
        ));

        // 완료 알림 이벤트 (알림 서비스)
        Long helperId = notifyHelper ? agreement.getHelperId() : null;
        Long disabledId = notifyDisabled ? agreement.getDisabledId() : null;

        eventPublisher.publish(new ActivityCompletedEvent(
                engagement.getEngagementId(),
                agreement.getId(),
                helperId,
                disabledId,
                engagement.getActivityDate(),
                true
        ));

        // 리뷰 요청 이벤트 (알림 서비스)
        eventPublisher.publish(new ReviewRequestEvent(
                agreement.getId(),
                List.of(engagement.getEngagementId()),
                agreement.getHelperId(),
                agreement.getDisabledId()
        ));
    }
}
