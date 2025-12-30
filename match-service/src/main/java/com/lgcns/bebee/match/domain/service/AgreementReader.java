package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.repository.AgreementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementReader {
    private final AgreementRepository agreementRepository;
    
    @Transactional(readOnly = true)
    public Agreement getById(Long agreementId) {
        return agreementRepository.findById(agreementId)
                .orElseThrow(() -> MatchErrors.AGREEMENT_NOT_FOUND.toException());
    }

//    @Transactional(readOnly = true)
//    public List<Agreement> findAllByDateAndMember(LocalDate date, Long memberId) {
//
//    }
}