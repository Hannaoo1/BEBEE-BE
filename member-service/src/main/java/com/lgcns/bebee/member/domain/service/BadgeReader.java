package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.member.domain.entity.Badge;
import com.lgcns.bebee.member.domain.entity.vo.DisabilityCategoryType;
import com.lgcns.bebee.member.domain.repository.BadgeRepository;
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
public class BadgeReader {

    private final BadgeRepository badgeRepository;

    @Transactional(readOnly = true)
    public List<BadgeStatusInfo> getBadgeStatusList(Long helperId) {
        List<Badge> badges = badgeRepository.findByHelperId(helperId);

        Map<Long, Badge> badgeMap = badges.stream()
                .collect(Collectors.toMap(Badge::getDisabilityCategoryId, badge -> badge));

        List<BadgeStatusInfo> badgeStatusList = new ArrayList<>();

        for (DisabilityCategoryType category : DisabilityCategoryType.values()) {
            Badge badge = badgeMap.get(category.getId());
            if (badge != null) {
                badgeStatusList.add(new BadgeStatusInfo(
                        badge.getDisabilityCategoryId(),
                        badge.getCompletionCount(),
                        badge.getBadgeCode()
                ));
            } else {
                badgeStatusList.add(new BadgeStatusInfo(
                        category.getId(),
                        0,
                        null
                ));
            }
        }

        return badgeStatusList;
    }

    @Getter
    @AllArgsConstructor
    public static class BadgeStatusInfo {
        private final Long disabilityCategoryId;
        private final Integer count;
        private final String badgeCode;
    }
}
