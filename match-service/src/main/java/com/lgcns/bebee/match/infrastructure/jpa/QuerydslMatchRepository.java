package com.lgcns.bebee.match.infrastructure.jpa;

import com.lgcns.bebee.match.domain.entity.Match;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.infrastructure.jpa.dto.MatchSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static com.lgcns.bebee.match.domain.entity.QMatch.match;
import static com.lgcns.bebee.match.domain.entity.QAgreement.agreement;
import static com.lgcns.bebee.match.domain.entity.QAgreementPeriod.agreementPeriod;
import static com.lgcns.bebee.match.domain.entity.QAgreementSchedule.agreementSchedule;

@Component
@RequiredArgsConstructor
public class QuerydslMatchRepository {
    private final JPAQueryFactory queryFactory;

    public List<Match> searchMatches(MatchSearchCond cond) {
        return queryFactory
                .selectFrom(match)
                .join(match.agreement, agreement).fetchJoin()
                .join(agreement.period, agreementPeriod).fetchJoin()
                .where(
                        memberIdEq(cond.memberId()),
                        dateCondition(cond.date(), cond.dayOfWeek(), cond.type())
                )
                .fetch();
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return match.helperId.eq(memberId)
                .or(match.disabledId.eq(memberId));
    }

    private BooleanExpression dateCondition(LocalDate date, DayOfWeek dayOfWeek, EngagementType type) {
        // type이 null이면 DAY와 TERM 모두 포함
        if (type == null) {
            BooleanExpression dayCondition = agreement.type.eq(EngagementType.DAY)
                    .and(agreementPeriod.startDate.eq(date));

            BooleanExpression termCondition = agreement.type.eq(EngagementType.TERM)
                    .and(agreementPeriod.startDate.loe(date))
                    .and(agreementPeriod.endDate.goe(date))
                    .and(JPAExpressions
                            .selectOne()
                            .from(agreementSchedule)
                            .where(
                                    agreementSchedule.agreement.eq(agreement),
                                    agreementSchedule.dayOfWeek.eq(dayOfWeek)
                            )
                            .exists()
                    );

            return dayCondition.or(termCondition);
        }

        // type이 DAY인 경우
        if (type == EngagementType.DAY) {
            return agreement.type.eq(EngagementType.DAY)
                    .and(agreementPeriod.startDate.eq(date));
        }

        // type이 TERM인 경우
        return agreement.type.eq(EngagementType.TERM)
                .and(agreementPeriod.startDate.loe(date))
                .and(agreementPeriod.endDate.goe(date))
                .and(JPAExpressions
                        .selectOne()
                        .from(agreementSchedule)
                        .where(
                                agreementSchedule.agreement.eq(agreement),
                                agreementSchedule.dayOfWeek.eq(dayOfWeek)
                        )
                        .exists()
                );
    }
}
