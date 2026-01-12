package com.lgcns.bebee.member.domain.service;

import com.lgcns.bebee.member.domain.entity.vo.ReviewKeywordCount;
import com.lgcns.bebee.member.domain.repository.MatchReviewSyncRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewReader {

    private final MatchReviewSyncRepository matchReviewSyncRepository;

    public List<ReviewKeywordCount> getReceivedReviewKeywordCounts(Long memberId) {
        return matchReviewSyncRepository.countKeywordsByRevieweeId(memberId)
                .stream()
                .map(kc -> new ReviewKeywordCount(kc.getKeywordId(), kc.getCount()))
                .collect(Collectors.toList());
    }
}
