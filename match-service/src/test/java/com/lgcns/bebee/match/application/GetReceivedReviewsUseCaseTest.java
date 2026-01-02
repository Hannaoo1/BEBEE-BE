package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.GetReceivedReviewsUseCase;
import com.lgcns.bebee.match.domain.service.ReviewManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("받은 후기 조회 유스케이스 테스트")
class GetReceivedReviewsUseCaseTest {

    @Mock
    private ReviewManager reviewManager;

    @InjectMocks
    private GetReceivedReviewsUseCase useCase;

    @Test
    @DisplayName("받은 후기가 없는 경우 빈 통계 반환")
    void execute_noReviews_returnsEmptyStats() {
        // Given
        Long memberId = 100L;
        boolean isMyPage = true;

        GetReceivedReviewsUseCase.Param param =
                new GetReceivedReviewsUseCase.Param(memberId, isMyPage);

        given(reviewManager.findReceivedReviews(memberId))
                .willReturn(Collections.emptyList());

        // When
        GetReceivedReviewsUseCase.Result result = useCase.execute(param);

        // Then
        assertNotNull(result);
        assertNotNull(result.getStats());
        assertTrue(result.getStats().isEmpty());
    }
}