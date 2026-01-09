package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.domain.entity.Agreement;
import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.repository.EngagementRepository;
import com.lgcns.bebee.match.domain.service.AgreementReader;
import com.lgcns.bebee.match.domain.service.MatchReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateAgreementEngagementsUseCase implements UseCase<CreateAgreementEngagementsUseCase.Param, Void> {
    private final MatchReader matchReader;
    private final EngagementRepository engagementRepository;

    @Transactional
    @Override
    public Void execute(Param param) {
        Match match = matchReader.getById(param.matchId);
        Agreement agreement = match.getAgreement();

        // 활동 날짜들 조회
        List<LocalDate> engagementDates = agreement.getEngagementDates();

        // 각 날짜에 대해 Engagement 생성
        List<Engagement> engagements = engagementDates.stream()
                .map(date -> Engagement.create(
                        match,
                        agreement.getType(),
                        date
                ))
                .collect(Collectors.toList());

        // 대량 저장
        engagementRepository.saveAll(engagements);

        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long matchId;
    }
}