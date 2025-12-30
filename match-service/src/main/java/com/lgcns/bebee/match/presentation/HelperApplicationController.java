package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.ApplyHelperUseCase;
import com.lgcns.bebee.match.presentation.dto.req.HelperApplyReqDTO;
import com.lgcns.bebee.match.presentation.swagger.HelperApplicationSwagger;
import com.lgcns.bebee.match.presentation.swagger.MatchSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helper-applications")
@RequiredArgsConstructor
public class HelperApplicationController implements HelperApplicationSwagger {

    private final ApplyHelperUseCase applyHelperUseCase;

    @PostMapping
    public ResponseEntity<Void> apply(@RequestBody HelperApplyReqDTO request) {
        ApplyHelperUseCase.Param param = request.toParam();
        applyHelperUseCase.execute(param);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
