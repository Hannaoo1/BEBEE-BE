package com.lgcns.bebee.match.domain.repository;

import com.lgcns.bebee.match.domain.entity.Engagement;
import com.lgcns.bebee.match.domain.entity.QReview;
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

@Component
@RequiredArgsConstructor
public class EngagementRepositoryImpl implements EngagementRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public List<Engagement> searchEngagements(EngagementSearchCond cond) {
        QReview hReview = new QReview("hReview");
        QReview dReview = new QReview("dReview");

        return queryFactory
                .selectFrom(engagement)
                .join(engagement.match, match).fetchJoin()
                .join(match.agreement, agreement).fetchJoin()
                .leftJoin(match.helperReview, hReview).fetchJoin()
                .leftJoin(match.disabledReview, dReview).fetchJoin()
                .where(
                        typeEq(cond.type()),
                        dateEq(cond.date())
                )
                .fetch();
    }

    private BooleanExpression typeEq(EngagementType type) {
        return type == null ? null : engagement.type.eq(type);
    }

    private BooleanExpression dateEq(LocalDate date) {
        return date == null ? null : engagement.date.eq(date);
    }
}
