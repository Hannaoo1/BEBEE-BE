package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.application.usecase.GetHelperBadgesUseCase;
import com.lgcns.bebee.match.presentation.dto.res.HelperBadgesGetResDTO;
import com.lgcns.bebee.match.presentation.swagger.BadgeSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/badges")
@RequiredArgsConstructor
public class BadgeController implements BadgeSwagger {

    private final GetHelperBadgesUseCase getHelperBadgesUseCase;

    @GetMapping
    @Override
    public ResponseEntity<HelperBadgesGetResDTO> getHelperBadges(
            @CurrentMember Long currentMemberId
    ) {
        GetHelperBadgesUseCase.Param param =
                new GetHelperBadgesUseCase.Param(currentMemberId);

        GetHelperBadgesUseCase.Result result =
                getHelperBadgesUseCase.execute(param);

        HelperBadgesGetResDTO response =
                HelperBadgesGetResDTO.from(result);

        return ResponseEntity.ok(response);
    }
}