package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.application.usecase.client.EventPublisher;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.common.data.event.PostCreatedEvent;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.PostManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatePostUseCase implements UseCase<CreatePostUseCase.Param, CreatePostUseCase.Result> {
    private final MemberManager memberManager;
    private final PostManager postManager;

    private final EventPublisher eventPublisher;

    @Transactional
    @Override
    public Result execute(Param param) {
        MemberSync member = memberManager.findExistingMember(param.memberId);

        List<DayOfWeek> daysOfWeek = new ArrayList<>();
        List<LocalTime> startTimes = new ArrayList<>();
        List<LocalTime> endTimes = new ArrayList<>();

        for (ScheduleParam schedule : param.schedules) {
            daysOfWeek.add(schedule.getDayOfWeek());
            startTimes.add(schedule.getStartTime());
            endTimes.add(schedule.getEndTime());
        }

        Post post = postManager.createPost(
                member.getId(),
                param.postType,
                param.postImages,
                param.title,
                param.helpCategoryIds,
                param.content,
                param.startDate,
                param.endDate,
                param.date,
                daysOfWeek,
                startTimes,
                endTimes,
                param.unitHoney,
                param.totalHoney,
                param.region,
                param.latitude,
                param.longitude
        );

        eventPublisher.publish(new PostCreatedEvent(post.getId(), post.getLatitude(), post.getLongitude()));

        return new Result(post.getId());
    }

    @Getter
    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long memberId;

        private final String postType;
        private final List<String> postImages;

        private final String title;
        private final List<Long> helpCategoryIds;
        private final String content;

        private final LocalDate startDate;
        private final LocalDate endDate;
        private final List<ScheduleParam> schedules;

        private final LocalDate date;
        private final Integer unitHoney;
        private final Integer totalHoney;
        
        private final String region;

        private final Double latitude;
        private final Double longitude;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ScheduleParam implements Params {
        private final DayOfWeek dayOfWeek;
        private final LocalTime startTime;
        private final LocalTime endTime;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Result {
        private final Long postId;
    }
}