package com.lgcns.bebee.member.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.member.application.usecase.GetProfileInfoUseCase;
import com.lgcns.bebee.member.application.usecase.ReadMemberProfileUseCase;
import com.lgcns.bebee.member.presentation.dto.res.MemberInfoResDTO;
import com.lgcns.bebee.member.presentation.dto.res.ProfileInfoResDTO;
import com.lgcns.bebee.member.presentation.swagger.MemberSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController implements MemberSwagger {

    private final ReadMemberProfileUseCase readMemberProfileUseCase;
    private final GetProfileInfoUseCase getProfileInfoUseCase;

    @Override
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResDTO> getMyInfo(@CurrentMember Long memberId) {
        ReadMemberProfileUseCase.Param param = new ReadMemberProfileUseCase.Param(memberId);
        ReadMemberProfileUseCase.Result result = readMemberProfileUseCase.execute(param);

        MemberInfoResDTO.MemberInfoResDTOBuilder builder = MemberInfoResDTO.builder()
                .memberId(String.valueOf(result.getMember().getId()))
                .email(result.getMember().getEmail())
                .name(result.getMember().getName())
                .nickname(result.getMember().getNickname())
                .role(result.getMember().getRole().name())
                .phoneNumber(result.getMember().getPhoneNumber())
                .introduction(result.getMember().getIntroduction() != null ? result.getMember().getIntroduction() : "")
                .latitude(result.getMember().getLatitude() != null ?
                        Double.valueOf(result.getMember().getLatitude()) : null)
                .longitude(result.getMember().getLongitude() != null ?
                        Double.valueOf(result.getMember().getLongitude()) : null)
                .profileImageUrl(
                        result.getMember().getProfileImageUrl() != null ? result.getMember().getProfileImageUrl() : "")
                .sweetness(result.getMember().getSweetness())
                .honeyPoint(0)
                .addressRoad(result.getMember().getAddressRoad() != null ? result.getMember().getAddressRoad() : "")
                .gender(result.getMember().getGender().name())
                .birthDate(result.getMember().getBirthDate())
                .ageGroup(result.getAgeGroup());

        if (result.getHelpTypes() != null) {
            builder.helpTypes(result.getHelpTypes());
        }

        if (result.getDocuments() != null) {
            builder.documents(result.getDocuments().stream()
                    .map(com.lgcns.bebee.member.presentation.dto.res.DocumentVerificationResDTO::from)
                    .collect(java.util.stream.Collectors.toList()));
        }

        if (result.getDisabilityType() != null) {
            builder.disabilityType(result.getDisabilityType())
                    .disabilityDescription(result.getDisabilityDescription());
        }

        if (result.getReviews() != null) {
            builder.reviews(result.getReviews().stream()
                    .map(r -> new MemberInfoResDTO.ReviewKeywordDTO(
                            r.keywordId(),
                            r.description(),
                            r.isPositive(),
                            r.count()
                    ))
                    .collect(java.util.stream.Collectors.toList()));
        }

        return ResponseEntity.ok(builder.build());
    }

    @Override
    @GetMapping("/profile/me")
    public ResponseEntity<ProfileInfoResDTO> getMyProfile(@CurrentMember Long memberId) {
        GetProfileInfoUseCase.Param param = new GetProfileInfoUseCase.Param(memberId);
        GetProfileInfoUseCase.Result result = getProfileInfoUseCase.execute(param);

        return ResponseEntity.ok(ProfileInfoResDTO.from(result));
    }

    @Override
    @GetMapping("/profile/{memberId}")
    public ResponseEntity<ProfileInfoResDTO> getMemberProfile(
            @CurrentMember Long currentMemberId,
            @PathVariable Long memberId
    ) {
        GetProfileInfoUseCase.Param param = new GetProfileInfoUseCase.Param(memberId);
        GetProfileInfoUseCase.Result result = getProfileInfoUseCase.execute(param);

        return ResponseEntity.ok(ProfileInfoResDTO.from(result));
    }
}
