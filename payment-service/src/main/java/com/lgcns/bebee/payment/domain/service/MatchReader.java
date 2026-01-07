package com.lgcns.bebee.payment.domain.service;

import com.lgcns.bebee.payment.domain.entity.sync.PaymentMatchSync;
import com.lgcns.bebee.payment.domain.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchReader {
    private final MatchRepository matchRepository;

    @Transactional(readOnly = true)
    public PaymentMatchSync findById(Long matchId) {
        return matchRepository.findById(matchId);
    }
}
