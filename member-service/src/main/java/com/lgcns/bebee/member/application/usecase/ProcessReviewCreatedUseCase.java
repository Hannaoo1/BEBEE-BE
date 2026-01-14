package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;
import com.lgcns.bebee.member.domain.entity.sync.MatchReviewSync;
import com.lgcns.bebee.member.domain.repository.MatchReviewSyncRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessReviewCreatedUseCase implements UseCase<ProcessReviewCreatedUseCase.Param, ProcessReviewCreatedUseCase.Result> {

    private final MatchReviewSyncRepository matchReviewSyncRepository;

    @Transactional
    @Override
    public Result execute(Param param) {
        param.validate();

        // 이미 동기화된 리뷰인지 확인
        if (matchReviewSyncRepository.existsById(param.getReviewId())) {
            return Result.alreadySynced();
        }

        // MatchReviewSync 생성
        MatchReviewSync reviewSync = MatchReviewSync.create(
                param.getReviewId(),
                param.getMatchId(),
                param.getReviewerId(),
                param.getRevieweeId(),
                param.getReviewDirection()
        );

        // 키워드 추가
        reviewSync.addKeywords(param.getKeywordIds());

        // 저장
        matchReviewSyncRepository.save(reviewSync);

        return Result.success();
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long reviewId;
        private final Long matchId;
        private final Long reviewerId;
        private final Long revieweeId;
        private final String reviewDirection;
        private final List<Integer> keywordIds;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(reviewId)) {
                throw new InvalidParamException(
                        MemberInvalidParamErrors.REQUIRED_FIELD,
                        "reviewId"
                );
            }
            if (!ParamValidator.isValidId(matchId)) {
                throw new InvalidParamException(
                        MemberInvalidParamErrors.REQUIRED_FIELD,
                        "matchId"
                );
            }
            if (!ParamValidator.isValidId(reviewerId)) {
                throw new InvalidParamException(
                        MemberInvalidParamErrors.REQUIRED_FIELD,
                        "reviewerId"
                );
            }
            if (!ParamValidator.isValidId(revieweeId)) {
                throw new InvalidParamException(
                        MemberInvalidParamErrors.REQUIRED_FIELD,
                        "revieweeId"
                );
            }
            if (keywordIds == null || keywordIds.isEmpty()) {
                throw new InvalidParamException(
                        MemberInvalidParamErrors.REQUIRED_FIELD,
                        "keywordIds"
                );
            }
            return true;
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private final boolean success;
        private final String message;

        public static Result success() {
            return new Result(true, "리뷰 동기화가 완료되었습니다.");
        }

        public static Result alreadySynced() {
            return new Result(true, "이미 동기화된 리뷰입니다.");
        }
    }
}
