package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Badge;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.repository.dto.BadgeRepository;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import com.lgcns.bebee.match.domain.service.MemberManager;
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
    
    private final AgreementReader agreementReader;
    private final MemberManager memberManager;
    private final BadgeRepository badgeRepository;
    
    @Transactional
    @Override
    public Result execute(Param param) {
        param.validate();
        
        // Agreement를 통해 도우미, 장애인 ID 정보 추출
        Agreement agreement = agreementReader.getById(param.getAgreementId());
        Long helperId = agreement.getHelperId();
        Long disabledId = agreement.getDisabledId();

        MemberSync disabledMember = memberManager.findExistingMember(disabledId);

        // 장애 유형 ID 추출
        List<Long> disabilityCategoryIds = disabledMember.getDisabilityCategories().stream()
                .map(dc -> dc.getId().getDisabilityCategoryId())
                .toList();

        // count 증가 + badgeCode 계산
        List<BadgeInfo> updatedBadges = new ArrayList<>();

        for (Long categoryId : disabilityCategoryIds) {

            // helper_id + disability_category_id로 Badge 조회
            Badge badge = badgeRepository
                    .findByHelperIdAndDisabilityCategoryId(helperId, categoryId)
                    .orElseGet(() -> {
                        return Badge.create(helperId, categoryId);
                    });

            int previousCount = badge.getCompletionCount();

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
                        MatchInvalidParamErrors.REQUIRED_FIELD,
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
