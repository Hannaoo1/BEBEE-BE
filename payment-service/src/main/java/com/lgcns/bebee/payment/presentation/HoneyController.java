package com.lgcns.bebee.payment.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.payment.application.usecase.GetHoneyBalanceUseCase;
import com.lgcns.bebee.payment.presentation.dto.res.HoneyBalanceGetResDTO;
import com.lgcns.bebee.payment.presentation.swagger.HoneySwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/honeys")
public class HoneyController implements HoneySwagger {

    private final GetHoneyBalanceUseCase getHoneyBalanceUseCase;

    @Override
    @GetMapping
    public ResponseEntity<HoneyBalanceGetResDTO> getHoneyBalanceByMemberId(
            @CurrentMember Long memberId
    ) {
        GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(memberId);
        GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);
        HoneyBalanceGetResDTO resDTO = HoneyBalanceGetResDTO.from(result);

        return ResponseEntity.ok().body(resDTO);
    }
}
