package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.client.EventPublisher;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.event.ActivityCompletedEvent;
import com.lgcns.bebee.match.domain.event.HoneySettlementEvent;
import com.lgcns.bebee.match.domain.event.ReviewRequestEvent;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import com.lgcns.bebee.match.domain.service.EngagementReader;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompleteEngagementUseCase
        implements UseCase<CompleteEngagementUseCase.Param, CompleteEngagementUseCase.Result> {

    private final EngagementReader engagementReader;
    private final AgreementReader agreementReader;
    private final EventPublisher eventPublisher;

    @Transactional
    @Override
    public Result execute(Param param) {


        param.validate();

        Engagement engagement = engagementReader.getById(param.getEngagementId());

        Agreement agreement = agreementReader.getById(engagement.getAgreementId());

        Role userType = param.getUserType();
        if (userType == Role.HELPER) {
            engagement.setHelperCheck();
        } else if (userType == Role.DISABLED) {
            engagement.setDisabledCheck();
        }

        // 케이스별 완료 여부 확인 및 처리
        if (engagement.isHelperCheck() && engagement.isDisabledCheck()) {

            // 케이스 1: 둘 다 완료 (즉시 완료)
            engagement.complete();
            publishCompletionEvents(engagement, agreement, true, true);

        } else if (engagement.isDisabledCheck()) {
            // 케이스 2: 장애인만 완료 (즉시 완료)
            engagement.complete();
            publishCompletionEvents(engagement, agreement, false, true);
        }

        // 케이스 3: 도우미만 완료 (PENDING 유지, 3일 후 스케줄러 처리)
        // 케이스 4: 둘 다 클릭 x (3일 후 스케줄러 처리)

        boolean isLastActivity = engagement.isLastActivity(agreement);

        return new Result(
                engagement.getStatus().name(),
                isLastActivity
        );
    }

    // 완료 처리 이벤트 발행
    private void publishCompletionEvents(
            Engagement engagement,
            Agreement agreement,
            boolean notifyHelper,
            boolean notifyDisabled
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

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final Long engagementId;
        private final Role userType;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(memberId)) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.REQUIRED_FIELD,
                        "memberId"
                );
            }
            if (!ParamValidator.isValidId(engagementId)) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.REQUIRED_FIELD,
                        "engagementId"
                );
            }
            if (userType == null) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.REQUIRED_FIELD,
                        "userType"
                );
            }
            if (userType != Role.HELPER && userType != Role.DISABLED) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.INVALID_FORMAT,
                        "userType"
                );
            }
            return true;
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private final String status;
        private final Boolean isLastActivity;
    }
}