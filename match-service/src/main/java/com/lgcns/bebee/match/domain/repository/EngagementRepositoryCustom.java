package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.repository.dto.EngagementSearchCond;

import java.util.List;

public interface EngagementRepositoryCustom {
    List<Engagement> searchEngagements(EngagementSearchCond cond);
}
