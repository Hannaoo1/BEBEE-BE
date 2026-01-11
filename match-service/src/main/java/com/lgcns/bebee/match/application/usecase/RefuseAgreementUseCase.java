package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.data.event.match.AgreementRefusedEvent;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.client.EventPublisher;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.lgcns.bebee.match.common.exception.MatchErrors.*;

@Service
@RequiredArgsConstructor
public class RefuseAgreementUseCase implements UseCase<RefuseAgreementUseCase.Param, Void> {
    private final AgreementReader agreementReader;

    private final EventPublisher eventPublisher;

    @Transactional
    @Override
    public Void execute(Param param) {
        param.validate();

        if(param.currentMemberId.equals(param.disabledId)){
            throw AGREEMENT_DISABLED_CANNOT_REFUSE.toException();
        }

        Agreement agreement = agreementReader.getById(param.getAgreementId());

        agreement.refuse();

        eventPublisher.publish(new AgreementRefusedEvent(
                param.chatroomId,
                param.chatId,
                agreement.getDisabledId(),
                agreement.getHelperId(),
                param.createdAt
        ));
        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long currentMemberId;
        private final Long disabledId;
        private final Long agreementId;
        private final Long chatroomId;
        private final Long chatId;
        private final LocalDateTime createdAt;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(agreementId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "agreementId");
            }

            return true;
        }
    }
}
