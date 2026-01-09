package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.common.util.ParamValidator;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.domain.repository.EngagementRepository;
import com.lgcns.bebee.match.presentation.dto.res.BadgeStatusDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GetHelperBadgesUseCase implements UseCase<GetHelperBadgesUseCase.Param, GetHelperBadgesUseCase.Result> {

    private final EngagementRepository engagementRepository;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {
        param.validate();

        // DB에서 도우미별 장애 유형별 count 조회
        List<Object[]> rawResults = engagementRepository
                .countCompletedEngagementsByDisabilityCategory(param.getHelperId());

        // Map으로 변환
        Map<Long, Integer> countMap = new HashMap<>();
        for (Object[] row : rawResults) {
            countMap.put((Long) row[0], ((Long) row[1]).intValue());
        }

        // 6개 장애 유형 모두 생성
        List<BadgeStatusDTO> badges = new ArrayList<>();
        for (Long categoryId = 1L; categoryId <= 6L; categoryId++) {
            Integer count = countMap.getOrDefault(categoryId, 0);
            String badgeCode = calculateBadgeCode(count);

            BadgeInfo info = new BadgeInfo(categoryId, count, badgeCode);
            badges.add(BadgeStatusDTO.from(info));
        }

        return new Result(badges);
    }

    // badgeCode 계산 로직
    private String calculateBadgeCode(Integer count) {
        if (count >= 10) {
            return "LEVEL_2";
        } else if (count >= 5) {
            return "LEVEL_1";
        } else {
            return null;
        }
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
    @RequiredArgsConstructor
    public static class BadgeInfo {
        private final Long categoryId;
        private final Integer count;
        private final String badgeCode;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result {
        private final List<BadgeStatusDTO> badges;
    }
}