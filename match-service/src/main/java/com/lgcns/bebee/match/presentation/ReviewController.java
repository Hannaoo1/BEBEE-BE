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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController implements ReviewSwagger {

    private final GetReviewKeywordsListUseCase getReviewKeywordsListUseCase;
    private final CreateReviewUseCase createReviewUseCase;

    @GetMapping("/keywords")
    public ResponseEntity<ReviewKeywordResDTO> getReviewKeywordsList(
            @CurrentMember Long currentMemberId
    ) {

        GetReviewKeywordsListUseCase.Param param = new GetReviewKeywordsListUseCase.Param(
                currentMemberId
        );

        GetReviewKeywordsListUseCase.Result result = getReviewKeywordsListUseCase.execute(param);

        // Result → ResDTO 변환
        ReviewKeywordResDTO resDTO = ReviewKeywordResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }

    @PostMapping
    public ResponseEntity<ReviewCreateResDTO> createReview(
            @CurrentMember Long currentMemberId,
            @Valid @RequestBody ReviewCreateReqDTO reqDTO
    ) {
        // ReqDTO → Param 변환
        List<Integer> keywordIdInts = reqDTO.getKeywordIds().stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        CreateReviewUseCase.Param param = new CreateReviewUseCase.Param(
                Long.parseLong(reqDTO.getEngagementId()),
                currentMemberId,
                Long.parseLong(reqDTO.getRevieweeId()),
                keywordIdInts
        );

        CreateReviewUseCase.Result result = createReviewUseCase.execute(param);

        ReviewCreateResDTO resDTO = ReviewCreateResDTO.from(result);

        return ResponseEntity.ok(resDTO);
    }
}