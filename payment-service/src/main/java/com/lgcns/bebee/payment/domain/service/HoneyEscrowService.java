package com.lgcns.bebee.payment.domain.service;

import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import com.lgcns.bebee.payment.domain.repository.HoneyEscrowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HoneyEscrowService {
    private final HoneyEscrowRepository honeyEscrowRepository;

    @Transactional(readOnly = true)
    public Boolean existsByMatchId(Long matchId) {
        if (honeyEscrowRepository.existsByMatchId(matchId)) {
            throw PaymentErrors.ESCROW_ALREADY_EXISTS.toException();
        }

        return true;
    }


}
