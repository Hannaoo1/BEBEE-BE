package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import com.lgcns.bebee.match.domain.service.EngagementReader;
import com.lgcns.bebee.match.domain.service.MemberManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteEngagementUseCase implements UseCase<CompleteEngagementUseCase.Param, CompleteEngagementUseCase.Result> {

    private final EngagementReader engagementReader;
    private final AgreementReader agreementReader;
    private final MemberManager memberManager;
    //private final EventPublisher eventPublisher;

    @Transactional
    @Override
    public Result execute(Param param) {
        
        param.validate();

        MemberSync member = memberManager.findExistingMember(param.getMemberId());

        Engagement engagement = engagementReader.getById(param.getEngagementId());

        Agreement agreement = agreementReader.getById(engagement.getAgreementId());

        Role userType = member.getRole();
        
        if (userType == Role.DISABLED) {
            // 케이스 1: 장애인만 완료시 (즉시 완료)
            engagement.complete();
            // publishCompletionEvents(engagement, agreement, false, true);

        } else if (userType == Role.HELPER) {
            // 케이스 2: 도우미만 완료 (PENDING 유지, 3일 후 스케줄러 처리)
            // 미완료 상태 유지, 스케줄러에서 처리
            engagement.setHelperCheck();
            // publishCompletionEvents(engagement, agreement, false, true);
        }
        
        // 마지막 활동 날짜 체크
        boolean isLastActivity = engagement.isLastActivity(agreement);

        return new Result(
                engagement.getStatus().name(),
                isLastActivity
        );
    }

    // 완료 처리 이벤트 발행
    /*
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

        // 리뷰 요청 이벤트, 장애인일 완료 하여 완전 완료 시점 (알림 서비스)
        if (engagement.getStatus() == EngagementStatus.COMPLETED) {
            eventPublisher.publish(new ReviewRequestEvent(
                    agreement.getId(),
                    List.of(engagement.getEngagementId()),
                    agreement.getHelperId(),
                    agreement.getDisabledId()
            ));
        }
    }
     */

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
        private final Long engagementId;

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