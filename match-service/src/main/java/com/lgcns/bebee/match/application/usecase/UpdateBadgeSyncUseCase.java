package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.domain.entity.sync.BadgeSync;
import com.lgcns.bebee.match.domain.repository.BadgeSyncRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateBadgeSyncUseCase implements UseCase<UpdateBadgeSyncUseCase.Param, UpdateBadgeSyncUseCase.Result> {

    private final BadgeSyncRepository badgeSyncRepository;

    @Transactional
    @Override
    public Result execute(Param param) {
        log.info("뱃지 동기화 시작 - helperId: {}, badges: {}", param.getHelperId(), param.getBadges().size());

        Long helperId = param.getHelperId();

        for (BadgeInfo badgeInfo : param.getBadges()) {
            // 기존 BadgeSync 조회 또는 새로 생성
            BadgeSync badgeSync = badgeSyncRepository
                    .findByHelperIdAndDisabilityCategoryId(helperId, badgeInfo.getDisabilityCategoryId())
                    .orElseGet(() -> BadgeSync.create(helperId, badgeInfo.getDisabilityCategoryId()));

            // 도메인 메서드로 업데이트
            badgeSync.updateBadge(badgeInfo.getCompletionCount(), badgeInfo.getBadgeCode());

            badgeSyncRepository.save(badgeSync);

            log.info("BadgeSync 업데이트 완료 - helperId: {}, categoryId: {}, count: {}, code: {}",
                    helperId, badgeInfo.getDisabilityCategoryId(),
                    badgeInfo.getCompletionCount(), badgeInfo.getBadgeCode());
        }

        return Result.success();
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long helperId;
        private final List<BadgeInfo> badges;
    }

    @Getter
    @RequiredArgsConstructor
    public static class BadgeInfo {
        private final Long disabilityCategoryId;
        private final Integer completionCount;
        private final String badgeCode;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result {
        private final String message;

        public static Result success() {
            return new Result("뱃지 동기화 완료");
        }
    }
}
