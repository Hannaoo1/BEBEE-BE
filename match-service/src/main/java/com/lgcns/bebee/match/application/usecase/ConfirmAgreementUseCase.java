package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.data.event.AgreementConfirmedEvent;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.application.usecase.client.EventPublisher;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.PostImage;
import com.lgcns.bebee.match.domain.repository.MatchRepository;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import com.lgcns.bebee.match.domain.service.PostManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lgcns.bebee.match.common.exception.MatchErrors.*;

@Service
@RequiredArgsConstructor
public class ConfirmAgreementUseCase implements UseCase<ConfirmAgreementUseCase.Param, ConfirmAgreementUseCase.Result>{
    private final PostManager postManager;
    private final AgreementReader agreementReader;
    private final MatchRepository matchRepository;

    private final EventPublisher eventPublisher;

    @Transactional
    @Override
    public Result execute(ConfirmAgreementUseCase.Param param) {
        param.validate();

        if(param.currentMemberId.equals(param.disabledId)){
            throw AGREEMENT_DISABLED_CANNOT_CONFIRM.toException();
        }

        Agreement agreement = agreementReader.getById(param.getAgreementId());

        agreement.confirm();

        Post post = postManager.findSinglePost(param.postId);
        List<PostImage> postImages = post.getImages();

        Match match = Match.create(
                param.currentMemberId,
                param.getDisabledId(),
                param.getTitle(),
                postImages != null && !postImages.isEmpty() ? postImages.get(0).getImageUrl() : null,
                param.getChatRoomId(),
                agreement
        );
        Match savedMatch = matchRepository.save(match);

        eventPublisher.publish(new AgreementConfirmedEvent(
                        param.getChatRoomId(),
                        match.getDisabledId(),
                        match.getMatchId(),
                        match.getAgreement().getUnitHoney()
                    )
        );

        return Result.from(savedMatch);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long currentMemberId;
        private final Long disabledId;
        private final Long postId;
        private final String title;
        private final Long chatRoomId;
        private final Long agreementId;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(disabledId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "disabledId");
            }
            if (!ParamValidator.isValidId(postId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "postId");
            }
            if (!ParamValidator.isValidString(title)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "title");
            }
            if (!ParamValidator.isValidStringLength(title, 1, 100)) {
                throw new InvalidParamException(MatchInvalidParamErrors.OUT_OF_RANGE, "title");
            }
            if (!ParamValidator.isValidId(chatRoomId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "chatRoomId");
            }
            if (!ParamValidator.isValidId(agreementId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "agreementId");
            }

            return true;
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private Long matchId;

        public static Result from(Match match) {
            return new Result(
                    match.getMatchId()
            );
        }
    }
}
