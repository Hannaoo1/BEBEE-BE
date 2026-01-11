package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.common.annotation.CurrentMember;
import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import com.lgcns.bebee.match.application.usecase.GetReviewKeywordsListUseCase;
import com.lgcns.bebee.match.presentation.dto.req.ReviewCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewKeywordResDTO;
import com.lgcns.bebee.match.presentation.swagger.ReviewSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 리뷰 API Controller
@RestController
@RequiredArgsConstructor
public class ReviewController implements ReviewSwagger {

    private final GetReviewKeywordsListUseCase getReviewKeywordsListUseCase;
    private final CreateReviewUseCase createReviewUseCase;

    // 리뷰 키워드 목록 조회
    @Override
    @GetMapping("/reviews/keywords")
    public ResponseEntity<ReviewKeywordResDTO> getReviewKeywordsList(
            @CurrentMember Long currentMemberId
    ) {
        GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(
                currentMemberId
        );

        GetReviewKeywordsListUseCase.Result result = getReviewKeywordsListUseCase.execute(param);

        ReviewKeywordResDTO resDTO = ReviewKeywordResDTO.from(result);

        return ResponseEntity.ok().body(resDTO);
    }

    // 리뷰 작성
    @Override
    @PostMapping("/matches/{matchId}/reviews")
    public ResponseEntity<ReviewCreateResDTO> createReview(
            @PathVariable Long matchId,
            @CurrentMember Long currentMemberId,
            @Valid @RequestBody ReviewCreateReqDTO reqDTO
    ) {
        CreateReviewUseCase.Param param = reqDTO.toParam(matchId, currentMemberId);

        CreateReviewUseCase.Result result = createReviewUseCase.execute(param);

        ReviewCreateResDTO resDTO = ReviewCreateResDTO.from(result);

        return ResponseEntity.ok().body(resDTO);
    }
}