package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.match.common.exception.MatchErrors;
import com.lgcns.bebee.match.common.exception.MatchInvalidParamErrors;
import com.lgcns.bebee.match.common.util.ParamValidator;
import com.lgcns.bebee.match.domain.entity.Application;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.PostPeriod;
import com.lgcns.bebee.match.domain.entity.PostSchedule;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.EngagementType;
import com.lgcns.bebee.match.domain.entity.vo.PostStatus;
import com.lgcns.bebee.match.domain.repository.HelperApplicationRepository;
import com.lgcns.bebee.match.domain.repository.PostRepository;
import com.lgcns.bebee.match.domain.service.MemberManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetHelperApplicationPostsUseCase implements UseCase<GetHelperApplicationPostsUseCase.Param, GetHelperApplicationPostsUseCase.Result> {

    private final PostRepository postRepository;
    private final HelperApplicationRepository applicationRepository;
    private final MemberManager memberManager;

    @Transactional(readOnly = true)
    @Override
    public Result execute(Param param) {
        param.validate();

        MemberSync member = memberManager.findExistingMember(param.getMemberId());
        if (member.getRole() != Role.DISABLED) {
            throw MatchErrors.ONLY_DISABLED_MEMBERS_ALLOWED.toException();
        }

        List<Post> posts = postRepository.findAllByMemberId(param.getMemberId());

        List<PostSummary> postSummaries = posts.stream()
                .map(post -> {
                    List<Application> applications = applicationRepository.findAllByPost_Id(post.getId());
                    return PostSummary.from(post, applications);
                })
                .collect(Collectors.toList());

        return new Result(postSummaries);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;

        @Override
        public boolean validate() {
            if (!ParamValidator.isValidId(memberId)) {
                throw new InvalidParamException(MatchInvalidParamErrors.REQUIRED_FIELD, "memberId");
            }
            return true;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Result {
        private final List<PostSummary> posts;
    }

    @Getter
    @AllArgsConstructor
    public static class PostSummary {
        private Long postId;
        private String title;
        private String region;
        private Integer commonApplicantCount;
        private Integer volunteerApplicantCount;
        private Boolean isMatched;
        private Integer daysRemaining;
        private Object engagementTime;
        private List<Integer> helpCategories;

        public static PostSummary from(Post post, List<Application> applications) {
            int commonCount = (int) applications.stream()
                    .filter(app -> !app.getIsVolunteer())
                    .count();

            int volunteerCount = (int) applications.stream()
                    .filter(Application::getIsVolunteer)
                    .count();

            Integer daysRemaining = calculateDaysRemaining(post.getPeriod());

            Object engagementTime = createEngagementTime(post);

            List<Integer> helpCategories = post.getHelpCategories().stream()
                    .map(helpCategory -> helpCategory.getId().getHelpCategoryId().intValue())
                    .collect(Collectors.toList());

            return new PostSummary(
                    post.getId(),
                    post.getTitle(),
                    post.getRegion(),
                    commonCount,
                    volunteerCount,
                    post.getStatus() == PostStatus.MATCHED,
                    daysRemaining,
                    engagementTime,
                    helpCategories
            );
        }

        private static Integer calculateDaysRemaining(PostPeriod period) {
            if (period == null || period.getEndDate() == null) {
                return null;
            }

            LocalDate now = LocalDate.now();
            LocalDate endDate = period.getEndDate();

            if (endDate.isBefore(now)) {
                return 0;
            }

            return (int) ChronoUnit.DAYS.between(now, endDate);
        }

        private static Object createEngagementTime(Post post) {
            if (post.getPeriod() == null) {
                return null;
            }

            if (post.getType() == EngagementType.DAY) {
                return DayEngagementTime.from(post);
            } else if (post.getType() == EngagementType.TERM) {
                return TermEngagementTime.from(post);
            }

            return null;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class DayEngagementTime {
        private LocalDate date;
        private ScheduleInfo schedule;

        public static DayEngagementTime from(Post post) {
            if (post.getPeriod() == null || post.getSchedules().isEmpty()) {
                return null;
            }
            
            return new DayEngagementTime(
                    post.getPeriod().getStartDate(),
                    ScheduleInfo.from(post.getSchedules().get(0))
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class TermEngagementTime {
        private LocalDate startDate;
        private LocalDate endDate;
        private List<ScheduleInfo> schedules;

        public static TermEngagementTime from(Post post) {
            if (post.getPeriod() == null) {
                return null;
            }
            
            List<ScheduleInfo> scheduleInfos = post.getSchedules().stream()
                    .map(ScheduleInfo::from)
                    .collect(Collectors.toList());

            return new TermEngagementTime(
                    post.getPeriod().getStartDate(),
                    post.getPeriod().getEndDate(),
                    scheduleInfos
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ScheduleInfo {
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;

        static ScheduleInfo from(PostSchedule schedule) {
            return new ScheduleInfo(
                    schedule.getDayOfWeek(),
                    schedule.getStartTime(),
                    schedule.getEndTime()
            );
        }
    }
}