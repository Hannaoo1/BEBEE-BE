package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.member.core.exception.MemberErrors;
import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;
import com.lgcns.bebee.member.domain.entity.Badge;
import com.lgcns.bebee.member.domain.entity.MemberDisabilityCategory;
import com.lgcns.bebee.member.domain.entity.sync.AgreementSync;
import com.lgcns.bebee.member.domain.repository.AgreementRepository;
import com.lgcns.bebee.member.domain.repository.BadgeRepository;
import com.lgcns.bebee.member.domain.repository.MemberDisabilityCategoryRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateBadgeUseCase implements UseCase<CreateBadgeUseCase.Param, CreateBadgeUseCase.Result> {

    private final AgreementRepository agreementRepository;
    private final MemberDisabilityCategoryRepository memberDisabilityCategoryRepository;
    private final BadgeRepository badgeRepository;

    @Transactional
    @Override
    public Result execute(Param param) {
        param.validate();

        // AgreementSync를 통해 도우미, 장애인 ID 정보 추출
        AgreementSync agreement = agreementRepository.findById(param.getAgreementId())
                .orElseThrow(() -> MemberErrors.AGREEMENT_NOT_FOUND.toException());

        Long helperId = agreement.getHelperId();
        Long disabledId = agreement.getDisabledId();

        // 장애인의 장애 유형 조회
        List<MemberDisabilityCategory> disabilityCategories = memberDisabilityCategoryRepository
                .findByMember_Id(disabledId);

        // 장애 유형 ID 추출
        List<Long> disabilityCategoryIds = disabilityCategories.stream()
                .map(dc -> dc.getDisabilityCategory().getDisabilityCategoryId())
                .toList();

        // count 증가 + badgeCode 계산
        List<BadgeInfo> updatedBadges = new ArrayList<>();

        for (Long categoryId : disabilityCategoryIds) {

            // helper_id + disability_category_id로 Badge 조회
            Badge badge = badgeRepository
                    .findByHelperIdAndDisabilityCategoryId(helperId, categoryId)
                    .orElseGet(() -> Badge.create(helperId, categoryId));

            // count 증가 + badgeCode 자동 계산
            badge.incrementCompletionCount();

            int newCount = badge.getCompletionCount();
            String badgeCode = badge.getBadgeCode();

            badgeRepository.save(badge);

            updatedBadges.add(new BadgeInfo(badge.getId(), helperId, categoryId, newCount, badgeCode));
        }
        return Result.from(updatedBadges);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long agreementId;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(agreementId)) {
                throw new InvalidParamException(
                        MemberInvalidParamErrors.REQUIRED_FIELD,
                        "agreementId"
                );
            }
            return true;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class BadgeInfo {
        private final Long badgeId;
        private final Long helperId;
        private final Long disabilityCategoryId;
        private final Integer completionCount;
        private final String badgeCode;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private final List<BadgeInfo> badges;

        public static Result from(List<BadgeInfo> badges) {
            return new Result(badges);
        }
    }
}
