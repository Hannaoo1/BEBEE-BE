package com.lgcns.bebee.match.presentation.dto.res;

import com.lgcns.bebee.match.application.usecase.GetHelperApplicationPostsUseCase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HelperApplicationPostsGetResDTO {
    private List<GetHelperApplicationPostsUseCase.PostSummary> posts;

    public static HelperApplicationPostsGetResDTO from(GetHelperApplicationPostsUseCase.Result result) {
        return new HelperApplicationPostsGetResDTO(result.getPosts());
    }
}
