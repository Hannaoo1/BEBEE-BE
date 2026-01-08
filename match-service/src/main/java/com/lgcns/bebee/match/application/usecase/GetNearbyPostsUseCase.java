package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.PostSchedule;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.LocationSearchType;
import com.lgcns.bebee.match.domain.repository.PostRepository;
import com.lgcns.bebee.match.domain.service.MemberManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetNearbyPostsUseCase implements UseCase<GetNearbyPostsUseCase.Param, GetNearbyPostsUseCase.Result> {
    private final MemberManager memberManager;

    private final PostRepository postRepository;

    @Override
    public Result execute(Param params) {
        MemberSync currentMember = memberManager.findExistingMember(params.currentMemberId);

        if (currentMember.getRole() == Role.DISABLED) {
            throw MatchErrors.ONLY_HELPER_MEMBERS_ALLOWED.toException();
        }

        List<Post> posts = findNearbyPosts(currentMember, params.type, params.longitude, params.latitude, params.radius);


        return Result.from(posts);
    }

    private List<Post> findNearbyPosts(MemberSync currentMember, LocationSearchType type, Double longitude, Double latitude, Integer radius){
        if(type == LocationSearchType.HOME){
            return postRepository.findPostsWithRadius(currentMember.getLongitude(), currentMember.getLatitude(), radius);
        }

        return postRepository.findPostsWithRadius(longitude, latitude, radius);
    }

    @RequiredArgsConstructor
    public static class Param implements Params{
        private final Long currentMemberId;
        private final LocationSearchType type;
        private final Double latitude;
        private final Double longitude;
        private final Integer radius;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result{
        private final List<PostDTO> nearbyPosts;

        public static Result from(List<Post> posts) {
            List<PostDTO> postDTOs = posts.stream()
                    .map(PostDTO::from)
                    .collect(Collectors.toList());

            return new Result(postDTOs);
        }

        @Getter
        @RequiredArgsConstructor
        public static class PostDTO {
            private final Long postId;
            private final String title;
            private final String legalDongName;
            private final List<Long> helpCategoryIds;
            private final LocalDate date;  // DAY 타입일 때만 값 있음
            private final List<String> dayOfWeeks;

            private final Double latitude;
            private final Double longitude;

            public static PostDTO from(Post post) {

                List<Long> helpCategoryIds = post.getHelpCategories().stream()
                        .map(postHelpCategory -> postHelpCategory.getId().getHelpCategoryId())
                        .collect(Collectors.toList());

                LocalDate date = null;

                if (post.getType() == EngagementType.DAY && post.getPeriod() != null) {
                    date = post.getPeriod().getStartDate();
                }
                List<String> dayOfWeeks = post.getSchedules().stream()
                        .map(PostSchedule::getDayOfWeek)
                        .distinct()
                        .sorted()
                        .map(Enum::name)
                        .collect(Collectors.toList());

                return new PostDTO(
                        post.getId(),
                        post.getTitle(),
                        post.getRegion(),
                        helpCategoryIds,
                        date,
                        dayOfWeeks,
                        post.getLatitude(),
                        post.getLongitude()
                );
            }
        }
    }
}
