package com.lgcns.bebee.match.common.exception;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.ErrorInfo;
import org.springframework.http.HttpStatus;

public enum MatchErrors implements ErrorInfo {
    POST_NOT_FOUND("존재하지 않는 게시글입니다.", HttpStatus.NOT_FOUND),

    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),

    MATCH_NOT_FOUND("매칭 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AGREEMENT_NOT_FOUND("매칭 확인서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FORBIDDEN("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ALREADY_CONFIRMED_AGREEMENT("이미 수락된 매칭 확인서입니다.", HttpStatus.CONFLICT),
    CANNOT_REFUSE_CONFIRMED_AGREEMENT("이미 수락된 매칭 확인서는 거절할 수 없습니다.", HttpStatus.CONFLICT),

    ENGAGEMENT_NOT_FOUND("활동을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    ENGAGEMENT_NOT_COMPLETED("완료된 활동만 리뷰를 작성할 수 있습니다", HttpStatus.FORBIDDEN),

    ALREADY_REVIEWED("이미 작성한 리뷰입니다", HttpStatus.CONFLICT),
    INVALID_KEYWORD("유효하지 않은 키워드입니다", HttpStatus.BAD_REQUEST),
    KEYWORD_DIRECTION_MISMATCH("리뷰 방향과 키워드가 일치하지 않습니다", HttpStatus.BAD_REQUEST),
    NOT_ENGAGEMENT_MEMBER("활동 참여자만 리뷰를 작성할 수 있습니다", HttpStatus.FORBIDDEN);

    private final String desc;
    private final HttpStatus httpStatus;

    MatchErrors(String desc, HttpStatus httpStatus) {
        this.desc = desc;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public DomainException toException() {
        return new MatchException(this);
    }
}
