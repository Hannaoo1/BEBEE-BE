package com.lgcns.bebee.member.domain.entity.vo;

/**
 * 리뷰 키워드별 개수를 나타내는 Value Object
 *
 * @param keywordId 키워드 ID
 * @param count 해당 키워드를 받은 횟수
 */
public record ReviewKeywordCount(Integer keywordId, Long count) {
}
