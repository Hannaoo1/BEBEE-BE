package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.domain.entity.Badge;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.repository.dto.BadgeRepository;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.presentation.dto.res.BadgeStatusDTO;
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
public class GetHelperBadgesUseCase implements UseCase<GetHelperBadgesUseCase.Param, GetHelperBadgesUseCase.Result> {

    private final BadgeRepository badgeRepository;
    private final MemberManager memberManager;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {
        param.validate();

        // 도우미 권한 검증
        MemberSync member = memberManager.findExistingMember(param.getHelperId());
        if (member.getRole() != Role.HELPER) {
            throw MatchErrors.HELPER_ONLY_CAN_APPLY.toException();
        }

        // 도우미의 모든 Badge 조회
        List<Badge> badges = badgeRepository.findAllByHelperId(param.getHelperId());

        // 6개 장애 유형 모두 표시 (없으면 count=0, badgeCode=null)
        List<BadgeStatusDTO> results = new ArrayList<>();

        for (Long categoryId = 1L; categoryId <= 6L; categoryId++) {

            Badge badge = badges.stream()
                    .filter(b -> b.getDisabilityCategoryId().equals(categoryId))
                    .findFirst()
                    .orElse(null);

            BadgeStatusDTO dto;
            if (badge != null) {
                dto = BadgeStatusDTO.from(badge);
            } else {
                dto = new BadgeStatusDTO(
                        List.of(categoryId.intValue()),
                        0,
                        null
                );
            }
            results.add(dto);
        }

        return new Result(results);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long helperId;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(helperId)) {
                throw new InvalidParamException(
                        MatchInvalidParamErrors.REQUIRED_FIELD,
                        "helperId"
                );
            }
            return true;
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private final List<BadgeStatusDTO> badges;
    }
}
