package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetMatchesByDateUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchesByDateGetResDTO {
    private List<GetMatchesByDateUseCase.MatchInfo> matches;

    public static MatchesByDateGetResDTO from(GetMatchesByDateUseCase.Result result) {
        return new MatchesByDateGetResDTO(result.getMatchInfos());
    }
}
