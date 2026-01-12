package com.lgcns.bebee.member.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.util.AgeGroupCalculator;
import com.lgcns.bebee.member.domain.entity.Badge;
import com.lgcns.bebee.member.domain.entity.DocumentVerification;
import com.lgcns.bebee.member.domain.entity.Member;
import com.lgcns.bebee.member.domain.entity.MemberDisabilityCategory;
import com.lgcns.bebee.member.domain.entity.sync.MemberHoneyWalletSync;
import com.lgcns.bebee.member.domain.entity.vo.DisabilityCategoryType;
import com.lgcns.bebee.member.domain.entity.vo.Gender;
import com.lgcns.bebee.member.domain.entity.vo.ReviewKeywordCount;
import com.lgcns.bebee.member.domain.entity.vo.Role;
import com.lgcns.bebee.member.domain.repository.*;
import com.lgcns.bebee.member.domain.service.HoneyWalletReader;
import com.lgcns.bebee.member.domain.service.MemberManagement;
import com.lgcns.bebee.member.domain.service.ReviewReader;
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
public class GetProfileInfoUseCase implements UseCase<GetProfileInfoUseCase.Param, GetProfileInfoUseCase.Result> {

    private final MemberManagement memberManagement;
    private final MemberHelpCategoryRepository memberHelpCategoryRepository;
    private final MemberDisabilityCategoryRepository memberDisabilityCategoryRepository;
    private final DocumentVerificationRepository documentVerificationRepository;
    private final BadgeRepository badgeRepository;
    private final HoneyWalletReader honeyWalletReader;
    private final ReviewReader reviewReader;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Param param) {
        Member member = memberManagement.getExistingMember(param.memberId);

        List<String> helpCategories = null;
        Long honey = null;
        List<ReviewKeywordCount> reviews = null;

        List<BadgeStatusInfo> badges = null;
        List<DocumentVerification> documents = null;

        String disabilityType = null;
        String disabilityDescription = null;

        helpCategories = memberHelpCategoryRepository.findByMember_Id(member.getId())
                .stream()
                .map(mhc -> mhc.getHelpCategory().getHelpType())
                .collect(Collectors.toList());

        MemberHoneyWalletSync wallet = honeyWalletReader.findByMemberId(member.getId());
        honey = wallet.getBalance() / 100;

        reviews = reviewReader.getReceivedReviewKeywordCounts(member.getId());

        if (member.getRole().equals(Role.HELPER)) {
            // 도우미: badges, documents 조회
            badges = getBadgeStatusList(member.getId());
            documents = documentVerificationRepository.findByMemberId(member.getId());

        } else if (member.getRole().equals(Role.DISABLED)) {
            // 장애인: disabilityType, disabilityDescription 조회
            List<MemberDisabilityCategory> disabilityCategories = memberDisabilityCategoryRepository
                    .findByMember_Id(member.getId());

            if (!disabilityCategories.isEmpty()) {
                MemberDisabilityCategory firstCategory = disabilityCategories.get(0);
                disabilityType = firstCategory.getDisabilityCategory().getType();
                disabilityDescription = firstCategory.getDisabilityDescription();
            }
        }

        return Result.from(
                member,
                honey,
                helpCategories,
                reviews,
                badges,
                documents,
                disabilityType,
                disabilityDescription
        );
    }

    // 도우미의 장애유형별 뱃지 상태 조회
    private List<BadgeStatusInfo> getBadgeStatusList(Long helperId) {
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
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;
    }

    @Getter
    @AllArgsConstructor
    public static class BadgeStatusInfo {
        private final Long disabilityCategoryId;
        private final Integer count;
        private final String badgeCode;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private String nickname;
        private String email;
        private Role role;
        private String profileImageUrl;
        private Gender gender;
        private Integer ageGroup;
        private String address;
        private List<String> helpCategories;
        private String introduction;
        private Long honey;
        private List<ReviewKeywordCount> reviews;
        /* 도우미 전용 정보 */
        private List<BadgeStatusInfo> badges;
        private List<DocumentVerification> documents;
        /* 장애인 전용 정보 */
        private String disabilityType;
        private String disabilityDescription;

        public static Result from(
                Member member,
                Long honey,
                List<String> helpCategories,
                List<ReviewKeywordCount> reviews,
                List<BadgeStatusInfo> badges,
                List<DocumentVerification> documents,
                String disabilityType,
                String disabilityDescription
        ) {
            Integer ageGroup = AgeGroupCalculator.calculateAgeGroup(member.getBirthDate());

            return new Result(
                    member.getNickname(),
                    member.getEmail(),
                    member.getRole(),
                    member.getProfileImageUrl(),
                    member.getGender(),
                    ageGroup,
                    member.getAddressRoad(),
                    helpCategories,
                    member.getIntroduction(),
                    honey,
                    reviews,
                    badges,
                    documents,
                    disabilityType,
                    disabilityDescription
            );
        }
    }
}
