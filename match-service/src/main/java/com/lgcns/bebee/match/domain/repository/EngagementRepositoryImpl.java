package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.repository.dto.EngagementSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.lgcns.bebee.match.domain.entity.QEngagement.engagement;
import static com.lgcns.bebee.match.domain.entity.QMatch.match;
import static com.lgcns.bebee.match.domain.entity.QAgreement.agreement;
import static com.lgcns.bebee.match.domain.entity.QReview.review;

@Component
@RequiredArgsConstructor
public class EngagementRepositoryImpl implements EngagementRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public List<Engagement> searchEngagements(EngagementSearchCond cond) {
        return queryFactory
                .selectFrom(engagement)
                .join(engagement.match, match).fetchJoin()
                .join(match.agreement, agreement).fetchJoin()
                .leftJoin(match.reviews, review).fetchJoin()
                .where(
                        memberIdEq(cond.memberId()),
                        typeEq(cond.type()),
                        dateEq(cond.date())
                )
                .distinct()
                .fetch();
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId == null ? null : match.helperId.eq(memberId).or(match.disabledId.eq(memberId));
    }

    private BooleanExpression typeEq(EngagementType type) {
        return type == null ? null : engagement.type.eq(type);
    }

    private BooleanExpression dateEq(LocalDate date) {
        return date == null ? null : engagement.date.eq(date);
    }
}
