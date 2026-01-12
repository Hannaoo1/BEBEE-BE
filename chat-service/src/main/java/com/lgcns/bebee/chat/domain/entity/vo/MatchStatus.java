package com.lgcns.bebee.chat.domain.entity.vo;

import com.lgcns.bebee.chat.core.exception.ChatInvalidParamErrors;

public enum MatchStatus {
    NON_MATCHED, PROCEEDING, MATCHED;

    public static MatchStatus from(String status){
        try{
            return valueOf(status.toUpperCase());
        }catch(IllegalArgumentException e){
            throw ChatInvalidParamErrors.INVALID_MATCH_STATUS.toException();
        }
    }
}
