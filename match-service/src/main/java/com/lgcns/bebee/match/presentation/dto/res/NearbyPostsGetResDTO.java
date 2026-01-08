package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetNearbyPostsUseCase;

import java.time.LocalDate;
import java.util.List;

public record NearbyPostsGetResDTO(
        List<NearbyPostDTO> nearByHelpers
) {

    public static NearbyPostsGetResDTO from(GetNearbyPostsUseCase.Result result){
        List<NearbyPostDTO> nearbyPostsDTO = result.getNearbyPosts()
                .stream()
                .map(postDTO -> new NearbyPostDTO(
                        postDTO.getPostId(),
                        postDTO.getTitle(),
                        postDTO.getLegalDongName(),
                        postDTO.getHelpCategoryIds(),
                        postDTO.getDate(),
                        postDTO.getDayOfWeeks(),
                        postDTO.getLatitude(),
                        postDTO.getLongitude()
                ))
                .toList();

        return new NearbyPostsGetResDTO(nearbyPostsDTO);
    }

    public record NearbyPostDTO(
            Long postId,
            String title,
            String legalDongName,
            List<Long> helpCategories,
            LocalDate date,  // DAY 타입일 때만 값 있음
            List<String> dayOfWeeks,
            Double latitude,
            Double longitude
    ){
    }
}
