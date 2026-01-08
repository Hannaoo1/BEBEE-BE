package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.application.usecase.GetNearbyHelpersUseCase;
import com.lgcns.bebee.match.application.usecase.GetNearbyPostsUseCase;
import com.lgcns.bebee.match.presentation.dto.req.NearbyHelpersGetReqDTO;
import com.lgcns.bebee.match.presentation.dto.req.NearbyPostsGetReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.NearbyHelpersGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.NearbyPostsGetResDTO;
import com.lgcns.bebee.match.presentation.swagger.MapSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/maps")
@RequiredArgsConstructor
public class MapController implements MapSwagger {
    private final GetNearbyHelpersUseCase getNearByHelpersUseCase;
    private final GetNearbyPostsUseCase getNearbyPostsUseCase;

    @Override
    @GetMapping("/nearby-helpers")
    public ResponseEntity<NearbyHelpersGetResDTO> getNearbyHelpers(
            @CurrentMember Long currentMemberId,
            @ModelAttribute NearbyHelpersGetReqDTO reqDTO
    ){
        GetNearbyHelpersUseCase.Param param = reqDTO.from(currentMemberId);
        GetNearbyHelpersUseCase.Result result = getNearByHelpersUseCase.execute(param);

        return ResponseEntity.ok(NearbyHelpersGetResDTO.from(result));
    }

    @Override
    @GetMapping("/nearby-posts")
    public ResponseEntity<NearbyPostsGetResDTO> getNearbyPosts(
            @CurrentMember Long currentMemberId,
            @ModelAttribute NearbyPostsGetReqDTO reqDTO
    ){
        GetNearbyPostsUseCase.Param param = reqDTO.from(currentMemberId);
        GetNearbyPostsUseCase.Result result = getNearbyPostsUseCase.execute(param);

        return ResponseEntity.ok(NearbyPostsGetResDTO.from(result));
    }
}
