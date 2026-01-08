package com.lgcns.bebee.match.presentation.dto.req;

import com.lgcns.bebee.match.application.usecase.GetNearbyPostsUseCase;
import com.lgcns.bebee.match.domain.entity.vo.LocationSearchType;
import io.swagger.v3.oas.annotations.media.Schema;

public record NearbyPostsGetReqDTO(
        @Schema(description = "위치 조회 기준 (CURRENT: 현재 위치, HOME: 등록된 주소", example = "CURRENT")
        LocationSearchType type,

        @Schema(description = "위도 (type이 CURRENT일 경우 필수")
        Double latitude,

        @Schema(description = "경도 (type이 CURRENT일 경우 필수")
        Double longitude,

        @Schema(description = "검색 반경 (km)", defaultValue = "3")
        Integer radius
) {

    public NearbyPostsGetReqDTO{
        if(radius == null) radius = 3;
    }

    public GetNearbyPostsUseCase.Param from(Long currentMemberId) {
        return new GetNearbyPostsUseCase.Param(
                currentMemberId,
                this.type,
                this.latitude,
                this.longitude,
                this.radius
        );
    }
}
