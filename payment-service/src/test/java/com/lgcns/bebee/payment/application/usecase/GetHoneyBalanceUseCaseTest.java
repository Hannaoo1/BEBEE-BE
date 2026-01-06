package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import com.lgcns.bebee.payment.domain.repository.HoneyWalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

/**
 * GetHoneyBalanceUseCase 단위 테스트
 *
 * 테스트 범위: 현재 꿀 잔액 조회 로직
 * Mock 대상: HoneyWalletRepository
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetHoneyBalanceUseCase 단위 테스트")
class GetHoneyBalanceUseCaseTest {

    @Mock
    private HoneyWalletRepository honeyWalletRepository;

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
            Long balance = 10000L; // 100꿀
            HoneyWallet mockWallet = mock(HoneyWallet.class);
            given(mockWallet.getBalance()).willReturn(balance);
            given(honeyWalletRepository.findByMemberId(testMemberId))
                    .willReturn(Optional.of(mockWallet));

            GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(testMemberId);

            // when
            GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getCurrentHoney()).isEqualTo(100L);
            then(honeyWalletRepository).should().findByMemberId(testMemberId);
        }

        @Test
        @DisplayName("지갑이 없으면 0을 반환한다")
        void execute_withNoWallet_returnsZero() {
            // given
            given(honeyWalletRepository.findByMemberId(testMemberId))
                    .willReturn(Optional.empty());

            GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(testMemberId);

            // when
            GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getCurrentHoney()).isEqualTo(0L);
            then(honeyWalletRepository).should().findByMemberId(testMemberId);
        }

        @Test
        @DisplayName("잔액이 0이면 0을 반환한다")
        void execute_withZeroBalance_returnsZero() {
            // given
            HoneyWallet mockWallet = mock(HoneyWallet.class);
            given(mockWallet.getBalance()).willReturn(0L);
            given(honeyWalletRepository.findByMemberId(testMemberId))
                    .willReturn(Optional.of(mockWallet));

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
            Long balance = 15050L; // 150.5꿀 -> 150꿀
            HoneyWallet mockWallet = mock(HoneyWallet.class);
            given(mockWallet.getBalance()).willReturn(balance);
            given(honeyWalletRepository.findByMemberId(testMemberId))
                    .willReturn(Optional.of(mockWallet));

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
            verifyHoneyCalculation(100L, 1L);
            verifyHoneyCalculation(500L, 5L);
            verifyHoneyCalculation(1000L, 10L);
            verifyHoneyCalculation(99L, 0L);
            verifyHoneyCalculation(101L, 1L);
            verifyHoneyCalculation(100000L, 1000L);
        }

        private void verifyHoneyCalculation(Long balance, Long expectedHoney) {
            HoneyWallet mockWallet = mock(HoneyWallet.class);
            given(mockWallet.getBalance()).willReturn(balance);
            given(honeyWalletRepository.findByMemberId(testMemberId))
                    .willReturn(Optional.of(mockWallet));

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
            Long balance = 50000L; // 500꿀
            HoneyWallet mockWallet = mock(HoneyWallet.class);
            given(mockWallet.getBalance()).willReturn(balance);
            given(honeyWalletRepository.findByMemberId(testMemberId))
                    .willReturn(Optional.of(mockWallet));

            GetHoneyBalanceUseCase.Param param = new GetHoneyBalanceUseCase.Param(testMemberId);

            // when
            GetHoneyBalanceUseCase.Result result = getHoneyBalanceUseCase.execute(param);

            // then
            assertThat(result.getCurrentHoney()).isEqualTo(500L);
        }
    }
}