package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.domain.entity.Badge;
import com.lgcns.bebee.match.domain.entity.sync.DisabilityCategory;
import com.lgcns.bebee.match.domain.repository.BadgeRepository;
import com.lgcns.bebee.match.presentation.dto.res.BadgeStatusDTO;
import lombok.AccessLevel;
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

    private final BadgeRepository badgeRepository;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {
        param.validate();

        Long helperId = param.getHelperId();

        // 해당 도우미의 모든 뱃지 조회
        List<Badge> badges = badgeRepository.findAllByHelperId(helperId);

        // 장애유형별 뱃지 맵 생성
        Map<Long, Badge> badgeMap = badges.stream()
                .collect(Collectors.toMap(Badge::getDisabilityCategoryId, badge -> badge));

        // 6개 장애유형에 대해 뱃지 정보 생성 (없으면 빈 뱃지)
        List<BadgeStatusDTO> badgeStatuses = new ArrayList<>();
        for (DisabilityCategory category : DisabilityCategory.values()) {
            Badge badge = badgeMap.get(category.getId());
            if (badge != null) {
                badgeStatuses.add(BadgeStatusDTO.from(badge));
            } else {
                badgeStatuses.add(BadgeStatusDTO.ofEmpty(category.getId()));
            }
        }

        return Result.from(badgeStatuses);
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

        public static Result from(List<BadgeStatusDTO> badges) {
            return new Result(badges);
        }
    }
}
