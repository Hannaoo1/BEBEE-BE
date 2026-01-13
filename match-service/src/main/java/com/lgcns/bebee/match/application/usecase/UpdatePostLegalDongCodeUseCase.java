package com.lgcns.bebee.match.application.usecase;

import com.lgcns.bebee.common.application.Params;
import com.lgcns.bebee.common.application.UseCase;
import com.lgcns.bebee.match.application.client.RegionCodeClient;
import com.lgcns.bebee.match.domain.entity.Post;
import com.lgcns.bebee.match.domain.service.PostManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdatePostLegalDongCodeUseCase implements UseCase<UpdatePostLegalDongCodeUseCase.Param, Void> {
    private final PostManager postManager;
    private final RegionCodeClient regionCodeClient;

    @Override
    @Transactional
    public Void execute(Param params) {
        String legalDongCode = regionCodeClient.resolveLegalDongCode(params.latitude, params.longitude);

        Post post = postManager.findSinglePost(params.postId);
        post.updateLegalDongCode(legalDongCode);

        return null;
    }

    @RequiredArgsConstructor
    public static class Param implements Params {
        private final Long postId;
        private final Double latitude;
        private final Double longitude;
    }
}
