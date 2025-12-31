package com.lgcns.bebee.match.presentation;

import com.lgcns.bebee.match.application.usecase.CreateReviewUseCase;
import com.lgcns.bebee.match.application.usecase.GetReviewKeywordsUseCase;
import com.lgcns.bebee.match.presentation.dto.req.ReviewCreateReqDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewCreateResDTO;
import com.lgcns.bebee.match.presentation.dto.res.ReviewKeywordResDTO;
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

    private final GetReviewKeywordsUseCase getReviewKeywordsUseCase;
    private final CreateReviewUseCase createReviewUseCase;

    @GetMapping("/keywords")
    @Override
    public ResponseEntity<List<ReviewKeywordResDTO>> getKeywords(
            @RequestParam Long engagementId,
            @RequestParam Long memberId
    ) {

        GetReviewKeywordsUseCase.Param param = new GetReviewKeywordsUseCase.Param(
                engagementId,
                memberId
        );

        GetReviewKeywordsUseCase.Result result = getReviewKeywordsUseCase.execute(param);

        List<ReviewKeywordResDTO> response = ReviewKeywordResDTO.fromList(result.getKeywords());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Override
    public ResponseEntity<ReviewCreateResDTO> createReview(
            @RequestParam  Long memberId,
            @Valid @RequestBody ReviewCreateReqDTO request
    ) {
        CreateReviewUseCase.Param param = request.toParam(memberId);

        CreateReviewUseCase.Result result = createReviewUseCase.execute(param);

        ReviewCreateResDTO response = ReviewCreateResDTO.from(result);

        return ResponseEntity.ok(response);
    }
}
