package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetHelperApplicationPostsUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperApplicationPostsGetResDTO {
    private List<PostSummaryDTO> posts;

    public static HelperApplicationPostsGetResDTO from(GetHelperApplicationPostsUseCase.Result result) {
         List<PostSummaryDTO> posts = result.getPosts().stream()
                .map(PostSummaryDTO::from).toList();

        return new HelperApplicationPostsGetResDTO(posts);
    }

    @Getter
    @AllArgsConstructor
    public static class PostSummaryDTO {
        private String postId;
        private String title;
        private String region;
        private Integer commonApplicantCount;
        private Integer volunteerApplicantCount;
        private Boolean isMatched;
        private Integer daysRemaining;
        private Object engagementTime;
        private List<Integer> helpCategories;

        public static PostSummaryDTO from(GetHelperApplicationPostsUseCase.PostSummary summary) {
            return new PostSummaryDTO(
                    String.valueOf(summary.getPostId()),
                    summary.getTitle(),
                    summary.getRegion(),
                    summary.getCommonApplicantCount(),
                    summary.getVolunteerApplicantCount(),
                    summary.getIsMatched(),
                    summary.getDaysRemaining(),
                    summary.getEngagementTime(),
                    summary.getHelpCategories()
            );
        }
    }
}
