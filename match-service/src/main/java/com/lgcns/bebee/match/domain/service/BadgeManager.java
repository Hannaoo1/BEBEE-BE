package com.lgcns.bebee.match.domain.service;

import com.lgcns.bebee.match.domain.entity.sync.BadgeSync;
import com.lgcns.bebee.match.domain.repository.BadgeSyncRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeManager {
    private final BadgeSyncRepository badgeSyncRepository;

    @Transactional(readOnly = true)
    public List<BadgeSync> findBadgesByHelperId(Long helperId) {
        return badgeSyncRepository.findByHelperId(helperId);
    }
}
