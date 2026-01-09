package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetMatchesByDateUseCase;

import java.time.LocalDate;
import java.util.List;

public record MatchesByDateResDTO(
    List<MatchDTO> engagements
) {
    public static MatchesByDateResDTO from(GetMatchesByDateUseCase.Result result) {

        return new MatchesByDateResDTO(result.getMatches().stream()
                .map(match -> new MatchDTO(
                        String.valueOf(match.getEngagementId()),
                        String.valueOf(match.getMatchId()),
                        String.valueOf(match.getAgreementId()),
                        String.valueOf(match.getOtherId()),
                        match.getOtherNickname(),
                        match.getThumbnailImageUrl(),
                        match.getTitle(),
                        String.valueOf(match.getChatRoomId()),
                        match.getRegion(),
                        match.getHelpType(),
                        match.getDate(),
                        match.getDayOfWeeks(),
                        match.getStatus(),
                        match.getHelpCategoryIds()
                ))
                .toList());
    }

    public record MatchDTO(
            String engagementId,
            String matchId,
            String agreementId,
            String otherId,
            String otherNickname,
            String thumbnailImageUrl,
            String title,
            String chatRoomId,
            String region,
            String helpType,
            LocalDate date,  // DAY 타입일 때만 값 있음
            List<String> dayOfWeeks,
            String status,
            List<Long> helpCategoryIds
    ){}
}
