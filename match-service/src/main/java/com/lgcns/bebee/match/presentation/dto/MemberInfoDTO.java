package com.lgcns.bebee.match.presentation.dto;


import com.lgcns.bebee.common.util.AgeGroupCalculator;
import com.lgcns.bebee.match.domain.entity.MatchMemberSync;
import com.lgcns.bebee.match.domain.entity.vo.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoDTO {
    private String id;
    private String nickname;
    private String profileImageUrl;
    private Gender gender;
    private Integer ageGroup;

    public static MemberInfoDTO from (MatchMemberSync member) {
        int ageGroup = AgeGroupCalculator.calculateAgeGroup(member.getBirthDate());

        return new MemberInfoDTO(
                String.valueOf(member.getId()),
                member.getNickname(),
                member.getProfileImageUrl(),
                member.getGender(),
                ageGroup
        );
    }
}
