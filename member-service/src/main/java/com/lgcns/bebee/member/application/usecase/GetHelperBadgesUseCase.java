package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.member.core.exception.MemberErrors;
import com.lgcns.bebee.member.core.exception.MemberInvalidParamErrors;
import com.lgcns.bebee.member.domain.entity.Badge;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.vo.DisabilityCategoryType;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import com.lgcns.bebee.member.domain.repository.BadgeRepository;
import com.lgcns.bebee.member.domain.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetHelperBadgesUseCase implements UseCase<GetHelperBadgesUseCase.Param, GetHelperBadgesUseCase.Result> {

    private final MemberRepository memberRepository;
    private final BadgeRepository badgeRepository;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {
        param.validate();

        Long helperId = param.getHelperId();

        // 도우미 역할 검증
        Member member = memberRepository.findById(helperId)
                .orElseThrow(() -> MemberErrors.MEMBER_NOT_FOUND.toException());

        if (member.getRole() != Role.HELPER) {
            throw MemberErrors.ONLY_HELPER_MEMBERS_ALLOWED.toException();
        }

        // 해당 도우미의 모든 뱃지 조회
        List<Badge> badges = badgeRepository.findByHelperId(helperId);

        // 장애유형별 뱃지 맵 생성
        Map<Long, Badge> badgeMap = badges.stream()
                .collect(Collectors.toMap(Badge::getDisabilityCategoryId, badge -> badge));

        // 존재하는 뱃지와 없는 카테고리 ID 분리
        List<Badge> existingBadges = new ArrayList<>();
        List<Long> missingCategoryIds = new ArrayList<>();

        for (DisabilityCategoryType category : DisabilityCategoryType.values()) {
            Badge badge = badgeMap.get(category.getId());
            if (badge != null) {
                existingBadges.add(badge);
            } else {
                missingCategoryIds.add(category.getId());
            }
        }

        return new Result(existingBadges, missingCategoryIds);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long helperId;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(helperId)) {
                throw new InvalidParamException(
                        MemberInvalidParamErrors.REQUIRED_FIELD,
                        "helperId"
                );
            }
            return true;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Result {
        private final List<Badge> existingBadges;
        private final List<Long> missingCategoryIds;
    }
}
