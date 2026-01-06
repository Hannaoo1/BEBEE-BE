package com.lgcns.bebee.payment.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.payment.application.usecase.GetHoneyBalanceUseCase;
import com.lgcns.bebee.payment.application.usecase.UseHoneyUseCase;
import com.lgcns.bebee.payment.presentation.dto.req.HoneyUseReqDTO;
import com.lgcns.bebee.payment.presentation.dto.res.HoneyBalanceGetResDTO;
import com.lgcns.bebee.payment.presentation.swagger.WalletSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallets")
public class WalletController implements WalletSwagger {

    private final GetHoneyBalanceUseCase getHoneyBalanceUseCase;
    private final UseHoneyUseCase useHoneyUseCase;

    @Override
    @GetMapping("/honeys")
    public ResponseEntity<HoneyBalanceGetResDTO> getHoneyBalanceByMemberId(
            @CurrentMember Long memberId
    ) {
        GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(memberId);
        GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);
        HoneyBalanceGetResDTO resDTO = HoneyBalanceGetResDTO.from(result);

        return ResponseEntity.ok().body(resDTO);
    }

    @Override
    @PostMapping("/usage")
    public ResponseEntity<Void> deductHoneyByMemberId(
            @CurrentMember Long memberId,
            @RequestBody HoneyUseReqDTO request
    ) {
        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(memberId, request.getUseHoney());
        useHoneyUseCase.execute(param);

        return ResponseEntity.ok().build();
    }
}
