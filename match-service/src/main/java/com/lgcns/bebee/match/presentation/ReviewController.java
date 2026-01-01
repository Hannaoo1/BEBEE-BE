package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import com.lgcns.bebee.match.application.usecase.GetReceivedReviewsUseCase;
import com.lgcns.bebee.match.application.usecase.GetReviewKeywordsListUseCase;
import com.lgcns.bebee.match.presentation.dto.req.ReviewCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewKeywordResDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewStatsResDTO;
import com.lgcns.bebee.match.presentation.swagger.ReviewSwagger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController implements ReviewSwagger {

    private final GetReviewKeywordsListUseCase getReviewKeywordsUseCase;
    private final CreateReviewUseCase createReviewUseCase;
    private final GetReceivedReviewsUseCase getReceivedReviewsUseCase;  // ← 추가!

    // 리뷰 키워드 목록 조회
    @Override
    @GetMapping("/keywords")
    public ResponseEntity<List<ReviewKeywordResDTO>> getKeywords(
            @RequestParam String currentMemberId
    ) {
        GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(
                Long.parseLong(currentMemberId)
        );

        GetReviewKeywordsListUseCase.Result result = getReviewKeywordsUseCase.execute(param);
        List<ReviewKeywordResDTO> resDTO = ReviewKeywordResDTO.fromList(result.getKeywords());

        return ResponseEntity.ok().body(resDTO);
    }

    // 리뷰 작성(키워드 선택)
    @Override
    @PostMapping
    public ResponseEntity<ReviewCreateResDTO> createReview(
            @RequestParam String currentMemberId,
            @Valid @RequestBody ReviewCreateReqDTO reqDTO
    ) {
        CreateReviewUseCase.Param param = reqDTO.toParam(Long.parseLong(currentMemberId));
        CreateReviewUseCase.Result result = createReviewUseCase.execute(param);
        ReviewCreateResDTO resDTO = ReviewCreateResDTO.from(result);

        return ResponseEntity.ok().body(resDTO);
    }

    // 받은 리뷰 통계 조회
    @Override
    @GetMapping("/received/{memberId}")
    public ResponseEntity<ReviewStatsResDTO> getReceivedReviews(
            @PathVariable String memberId,
            @RequestParam(defaultValue = "false") boolean isMyPage
    ) {
        GetReceivedReviewsUseCase.Param param = new GetReceivedReviewsUseCase.Param(
                Long.parseLong(memberId),
                isMyPage
        );

        GetReceivedReviewsUseCase.Result result = getReceivedReviewsUseCase.execute(param);
        ReviewStatsResDTO resDTO = ReviewStatsResDTO.from(result);

        return ResponseEntity.ok().body(resDTO);
    }
}
