package com.lgcns.bebee.match.domain.entity.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewDirection {
    
    // 장애인 -> 도우미
    DISABLED_TO_HELPER,
    // 도우미 -> 장애인
    HELPER_TO_DISABLED
}
