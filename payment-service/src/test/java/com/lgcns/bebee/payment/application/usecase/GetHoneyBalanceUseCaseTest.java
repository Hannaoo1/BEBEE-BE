package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.payment.domain.service.HoneyWalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * GetHoneyBalanceUseCase 단위 테스트
 *
 * 테스트 범위: 현재 꿀 잔액 조회 로직
 * Mock 대상: HoneyWalletService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetHoneyBalanceUseCase 단위 테스트")
class GetHoneyBalanceUseCaseTest {

    @Mock
    private HoneyWalletService honeyWalletService;

    @InjectMocks
    private GetHoneyBalanceUseCase getHoneyBalanceUseCase;

    private Long testMemberId;

    @BeforeEach
    void setUp() {
        testMemberId = 100L;
    }

    @Nested
    @DisplayName("execute() 메서드")
    class ExecuteTest {

        @Test
        @DisplayName("지갑이 존재하고 잔액이 있으면 꿀 개수를 반환한다")
        void execute_withExistingWallet_returnsHoneyCount() {
            // given
            Long expectedHoney = 100L;
            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(expectedHoney);

            GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(testMemberId);

            // when
            GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getCurrentHoney()).isEqualTo(100L);
            then(honeyWalletService).should().getCurrentHoney(testMemberId);
        }

        @Test
        @DisplayName("지갑이 없으면 0을 반환한다")
        void execute_withNoWallet_returnsZero() {
            // given
            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(0L);

            GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(testMemberId);

            // when
            GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getCurrentHoney()).isEqualTo(0L);
            then(honeyWalletService).should().getCurrentHoney(testMemberId);
        }

        @Test
        @DisplayName("잔액이 0이면 0을 반환한다")
        void execute_withZeroBalance_returnsZero() {
            // given
            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(0L);

            GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(testMemberId);

            // when
            GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getCurrentHoney()).isEqualTo(0L);
        }

        @Test
        @DisplayName("100으로 나눌 때 소수점은 버린다")
        void execute_withBalance_truncatesDecimal() {
            // given
            Long expectedHoney = 150L; // HoneyWalletService에서 이미 변환된 값
            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(expectedHoney);

            GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(testMemberId);

            // when
            GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);

            // then
            assertThat(result.getCurrentHoney()).isEqualTo(150L);
        }

        @Test
        @DisplayName("다양한 잔액에 대해 정확히 계산한다")
        void execute_withVariousBalances_calculatesCorrectly() {
            // given & when & then
            verifyHoneyCalculation(1L);
            verifyHoneyCalculation(5L);
            verifyHoneyCalculation(10L);
            verifyHoneyCalculation(0L);
            verifyHoneyCalculation(1000L);
        }

        private void verifyHoneyCalculation(Long expectedHoney) {
            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(expectedHoney);

            GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(testMemberId);
            GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);

            assertThat(result.getCurrentHoney()).isEqualTo(expectedHoney);
        }
    }

    @Nested
    @DisplayName("Param 테스트")
    class ParamTest {

        @Test
        @DisplayName("Param 객체가 정상적으로 생성된다")
        void param_createsSuccessfully() {
            // when
            GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(testMemberId);

            // then
            assertThat(param.getMemberId()).isEqualTo(testMemberId);
        }
    }

    @Nested
    @DisplayName("Result 검증")
    class ResultValidation {

        @Test
        @DisplayName("Result.from() 정적 팩토리 메서드가 정상 동작한다")
        void result_fromMethod_worksCorrectly() {
            // given
            Long expectedHoney = 500L;

            // when
            GetHoneyBalanceUseCase.Result result = GetHoneyBalanceUseCase.Result.from(expectedHoney);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getCurrentHoney()).isEqualTo(expectedHoney);
        }

        @Test
        @DisplayName("정상 실행 시 Result에 올바른 값이 담긴다")
        void execute_returnsCorrectResultValues() {
            // given
            Long expectedHoney = 500L;
            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(expectedHoney);

            GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(testMemberId);

            // when
            GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);

            // then
            assertThat(result.getCurrentHoney()).isEqualTo(500L);
        }
    }
}