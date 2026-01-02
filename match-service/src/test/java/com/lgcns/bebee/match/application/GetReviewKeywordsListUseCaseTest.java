package com.lgcns.bebee.match.application;

import com.lgcns.bebee.match.application.usecase.GetReviewKeywordsListUseCase;
import com.lgcns.bebee.match.domain.entity.sync.MemberSync;
import com.lgcns.bebee.match.domain.entity.sync.Role;
import com.lgcns.bebee.match.domain.entity.vo.ReviewDirection;
import com.lgcns.bebee.match.domain.service.MemberManager;
import com.lgcns.bebee.match.domain.service.ReviewManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)  // ← 추가!
@DisplayName("리뷰 키워드 목록 조회 유스케이스 테스트")
class GetReviewKeywordsListUseCaseTest {

    @Mock
    private MemberManager memberManager;

    @Mock
    private ReviewManager reviewManager;

    @Mock
    private MemberSync memberSync;

    @InjectMocks
    private GetReviewKeywordsListUseCase useCase;

    @Test
    @DisplayName("장애인 회원의 키워드 목록 조회")
    void execute_disabledMember_returnsKeywords() {
        // Given
        Long reviewerId = 100L;
        GetReviewKeywordsListUseCase.Param param =
                new GetReviewKeywordsListUseCase.Param(reviewerId);

        given(memberManager.findExistingMember(reviewerId)).willReturn(memberSync);
        given(memberSync.getRole()).willReturn(Role.DISABLED);
        given(reviewManager.determineReviewDirection(memberSync))
                .willReturn(ReviewDirection.DISABLED_TO_HELPER);

        // When
        GetReviewKeywordsListUseCase.Result result = useCase.execute(param);

        // Then
        assertNotNull(result);
        assertNotNull(result.getKeywords());
        assertEquals(13, result.getKeywords().size());
        assertFalse(result.getKeywords().isEmpty());
    }

    @Test
    @DisplayName("도우미 회원의 키워드 목록 조회")
    void execute_helperMember_returnsKeywords() {
        // Given
        Long reviewerId = 700L;
        GetReviewKeywordsListUseCase.Param param =
                new GetReviewKeywordsListUseCase.Param(reviewerId);

        given(memberManager.findExistingMember(reviewerId)).willReturn(memberSync);
        given(memberSync.getRole()).willReturn(Role.HELPER);
        given(reviewManager.determineReviewDirection(memberSync))
                .willReturn(ReviewDirection.HELPER_TO_DISABLED);

        // When
        GetReviewKeywordsListUseCase.Result result = useCase.execute(param);

        // Then
        assertNotNull(result);
        assertNotNull(result.getKeywords());
        assertEquals(11, result.getKeywords().size());
        assertFalse(result.getKeywords().isEmpty());
    }
}