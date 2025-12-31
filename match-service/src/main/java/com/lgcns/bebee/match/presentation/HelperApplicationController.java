package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.ApplyHelperUseCase;
import com.lgcns.bebee.match.application.usecase.GetHelperApplicationPostsUseCase;
import com.lgcns.bebee.match.application.usecase.GetHelperApplicationsByPostUseCase;
import com.lgcns.bebee.match.presentation.dto.req.HelperApplyReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.HelperApplicantsByPostGetResDTO;
import com.lgcns.bebee.match.presentation.dto.res.HelperApplicationPostsGetResDTO;
import com.lgcns.bebee.match.presentation.swagger.HelperApplicationSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/helper-applications")
@RequiredArgsConstructor
public class HelperApplicationController implements HelperApplicationSwagger {

    private final ApplyHelperUseCase applyHelperUseCase;
    private final GetHelperApplicationPostsUseCase getHelperApplicationPostsUseCase;
    private final GetHelperApplicationsByPostUseCase getHelperApplicationsByPostUseCase;

    @PostMapping
    public ResponseEntity<Void> apply(@RequestBody HelperApplyReqDTO request) {
        ApplyHelperUseCase.Param param = request.toParam();
        applyHelperUseCase.execute(param);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/posts")
    public ResponseEntity<HelperApplicationPostsGetResDTO> getHelperApplicationPosts(@RequestParam String memberId) {
        GetHelperApplicationPostsUseCase.Param param = new GetHelperApplicationPostsUseCase.Param(Long.parseLong(memberId));
        GetHelperApplicationPostsUseCase.Result result = getHelperApplicationPostsUseCase.execute(param);

        HelperApplicationPostsGetResDTO response = HelperApplicationPostsGetResDTO.from(result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{postId}/applicants")
    public ResponseEntity<HelperApplicantsByPostGetResDTO> getHelperApplicationsByPost(
            @PathVariable String postId,
            @RequestParam String memberId
    ) {
        GetHelperApplicationsByPostUseCase.Param param = new GetHelperApplicationsByPostUseCase.Param(
                Long.parseLong(memberId),
                Long.parseLong(postId)
        );
        GetHelperApplicationsByPostUseCase.Result result = getHelperApplicationsByPostUseCase.execute(param);

        HelperApplicantsByPostGetResDTO response = HelperApplicantsByPostGetResDTO.from(result);

        return ResponseEntity.ok(response);
    }
}
