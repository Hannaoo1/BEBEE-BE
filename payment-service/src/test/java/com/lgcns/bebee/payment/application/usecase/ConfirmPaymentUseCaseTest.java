package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.payment.application.client.TossPaymentsClient;
import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import com.lgcns.bebee.payment.common.exception.PaymentException;
import com.lgcns.bebee.payment.domain.entity.HoneyHistory;
import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import com.lgcns.bebee.payment.domain.entity.Payment;
import com.lgcns.bebee.payment.domain.entity.vo.HoneyHistoryType;
import com.lgcns.bebee.payment.domain.repository.HoneyHistoryRepository;
import com.lgcns.bebee.payment.domain.repository.HoneyWalletRepository;
import com.lgcns.bebee.payment.domain.repository.PaymentRepository;
import com.lgcns.bebee.payment.infrastructure.redis.RedisTempPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

/**
 * ConfirmPaymentUseCase 단위 테스트
 *
 * 테스트 범위: 결제 승인 유스케이스 로직
 * Mock 대상: RedisTempPaymentService, TossPaymentsClient, PaymentRepository, HoneyWalletRepository, HoneyHistoryRepository
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ConfirmPaymentUseCase 단위 테스트")
class ConfirmPaymentUseCaseTest {

    @Mock
    private RedisTempPaymentService redisTempPaymentService;

    @Mock
    private TossPaymentsClient tossPaymentsClient;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private HoneyWalletRepository honeyWalletRepository;

    @Mock
    private HoneyHistoryRepository honeyHistoryRepository;

    @InjectMocks
    private ConfirmPaymentUseCase confirmPaymentUseCase;

    private String testOrderId;
    private String testPaymentKey;
    private Integer testAmount;
    private Long testMemberId;

    @BeforeEach
    void setUp() {
        testOrderId = "0P2V16C5HW8MN";
        testPaymentKey = "5EnNZRJGvaBX7zk2yd8ydw26XvwXkLrx9POLqKQjmAw4b0e1";
        testAmount = 100000;
        testMemberId = 100L;
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("정상적인 결제 승인 플로우가 성공한다 (신규 지갑 생성)")
        void execute_withValidParams_succeedsWithNewWallet() {
            // given
            ConfirmPaymentUseCase.Param param = new ConfirmPaymentUseCase.Param(
                    testOrderId, testPaymentKey, testAmount, testMemberId
            );

            // Redis 임시 데이터
            RedisTempPaymentService.TempPaymentDto tempDto =
                    new RedisTempPaymentService.TempPaymentDto(testOrderId, testAmount, testMemberId);
            given(redisTempPaymentService.get(testOrderId)).willReturn(tempDto);

            // 토스 API 응답
            TossPaymentsClient.TossPaymentResponse tossResponse =
                    new TossPaymentsClient.TossPaymentResponse(
                            testPaymentKey, testOrderId, "DONE", testAmount,
                            "카드", "2024-01-01T00:00:00", "2024-01-01T00:00:01"
                    );
            given(tossPaymentsClient.confirmPayment(testPaymentKey, testOrderId, testAmount))
                    .willReturn(tossResponse);

            // Payment 저장
            Payment mockPayment = mock(Payment.class);
            given(mockPayment.getPaymentId()).willReturn(1L);
            given(paymentRepository.save(any(Payment.class))).willReturn(mockPayment);

            // HoneyWallet 생성 (신규)
            HoneyWallet mockWallet = mock(HoneyWallet.class);
            given(honeyWalletRepository.findByMemberIdWithLock(testMemberId)).willReturn(Optional.empty());
            given(honeyWalletRepository.save(any(HoneyWallet.class))).willReturn(mockWallet);

            // HoneyHistory 저장
            HoneyHistory mockHistory = mock(HoneyHistory.class);
            given(honeyHistoryRepository.save(any(HoneyHistory.class))).willReturn(mockHistory);

            // when
            ConfirmPaymentUseCase.Result result = confirmPaymentUseCase.execute(param);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getPaymentId()).isEqualTo("1");
            assertThat(result.getCurrentBalance()).isEqualTo(testAmount);
            assertThat(result.getPaymentKey()).isEqualTo(testPaymentKey);

            then(redisTempPaymentService).should().get(testOrderId);
            then(tossPaymentsClient).should().confirmPayment(testPaymentKey, testOrderId, testAmount);
            then(paymentRepository).should().save(any(Payment.class));
            then(honeyWalletRepository).should().findByMemberIdWithLock(testMemberId);
            then(honeyWalletRepository).should().save(any(HoneyWallet.class));
            then(honeyHistoryRepository).should().save(any(HoneyHistory.class));
            then(redisTempPaymentService).should().delete(testOrderId);
        }

        @Test
        @DisplayName("정상적인 결제 승인 플로우가 성공한다 (기존 지갑 충전)")
        void execute_withValidParams_succeedsWithExistingWallet() {
            // given
            ConfirmPaymentUseCase.Param param = new ConfirmPaymentUseCase.Param(
                    testOrderId, testPaymentKey, testAmount, testMemberId
            );

            RedisTempPaymentService.TempPaymentDto tempDto =
                    new RedisTempPaymentService.TempPaymentDto(testOrderId, testAmount, testMemberId);
            given(redisTempPaymentService.get(testOrderId)).willReturn(tempDto);

            TossPaymentsClient.TossPaymentResponse tossResponse =
                    new TossPaymentsClient.TossPaymentResponse(
                            testPaymentKey, testOrderId, "DONE", testAmount,
                            "카드", "2024-01-01T00:00:00", "2024-01-01T00:00:01"
                    );
            given(tossPaymentsClient.confirmPayment(testPaymentKey, testOrderId, testAmount))
                    .willReturn(tossResponse);

            Payment mockPayment = mock(Payment.class);
            given(mockPayment.getPaymentId()).willReturn(1L);
            given(paymentRepository.save(any(Payment.class))).willReturn(mockPayment);

            // 기존 지갑 (잔액 50,000원)
            HoneyWallet existingWallet = mock(HoneyWallet.class);
            given(existingWallet.getBalance()).willReturn(150000L); // 50,000 + 100,000
            given(honeyWalletRepository.findByMemberIdWithLock(testMemberId)).willReturn(Optional.of(existingWallet));
            given(honeyWalletRepository.save(any(HoneyWallet.class))).willReturn(existingWallet);

            HoneyHistory mockHistory = mock(HoneyHistory.class);
            given(honeyHistoryRepository.save(any(HoneyHistory.class))).willReturn(mockHistory);

            // when
            ConfirmPaymentUseCase.Result result = confirmPaymentUseCase.execute(param);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getCurrentBalance()).isEqualTo(150000);
            then(honeyWalletRepository).should().findByMemberIdWithLock(testMemberId);
        }
    }

    @Nested
    @DisplayName("Redis 검증 실패")
    class RedisValidationFailures {

        @Test
        @DisplayName("Redis에 결제 정보가 없으면 예외가 발생한다")
        void execute_withNoRedisData_throwsException() {
            // given
            ConfirmPaymentUseCase.Param param = new ConfirmPaymentUseCase.Param(
                    testOrderId, testPaymentKey, testAmount, testMemberId
            );

            given(redisTempPaymentService.get(testOrderId))
                    .willThrow(PaymentErrors.PAYMENT_NOT_FOUND.toException());

            // when & then
            assertThatThrownBy(() -> confirmPaymentUseCase.execute(param))
                    .isInstanceOf(PaymentException.class)
                    .hasMessageContaining("결제 정보를 찾을 수 없습니다");

            then(tossPaymentsClient).should(never()).confirmPayment(anyString(), anyString(), anyInt());
            then(paymentRepository).should(never()).save(any(Payment.class));
        }

        @Test
        @DisplayName("결제 금액이 일치하지 않으면 예외가 발생한다")
        void execute_withAmountMismatch_throwsException() {
            // given
            Integer differentAmount = 50000; // Redis: 100,000원, Param: 50,000원
            ConfirmPaymentUseCase.Param param = new ConfirmPaymentUseCase.Param(
                    testOrderId, testPaymentKey, differentAmount, testMemberId
            );

            RedisTempPaymentService.TempPaymentDto tempDto =
                    new RedisTempPaymentService.TempPaymentDto(testOrderId, testAmount, testMemberId);
            given(redisTempPaymentService.get(testOrderId)).willReturn(tempDto);

            // when & then
            assertThatThrownBy(() -> confirmPaymentUseCase.execute(param))
                    .isInstanceOf(PaymentException.class)
                    .hasMessageContaining("결제 금액이 일치하지 않습니다");

            then(tossPaymentsClient).should(never()).confirmPayment(anyString(), anyString(), anyInt());
            then(paymentRepository).should(never()).save(any(Payment.class));
        }

        @Test
        @DisplayName("회원 ID가 일치하지 않으면 예외가 발생한다")
        void execute_withMemberIdMismatch_throwsException() {
            // given
            Long differentMemberId = 200L; // Redis: 100L, Param: 200L
            ConfirmPaymentUseCase.Param param = new ConfirmPaymentUseCase.Param(
                    testOrderId, testPaymentKey, testAmount, differentMemberId
            );

            RedisTempPaymentService.TempPaymentDto tempDto =
                    new RedisTempPaymentService.TempPaymentDto(testOrderId, testAmount, testMemberId);
            given(redisTempPaymentService.get(testOrderId)).willReturn(tempDto);

            // when & then
            assertThatThrownBy(() -> confirmPaymentUseCase.execute(param))
                    .isInstanceOf(PaymentException.class)
                    .hasMessageContaining("결제 회원 정보가 일치하지 않습니다");

            then(tossPaymentsClient).should(never()).confirmPayment(anyString(), anyString(), anyInt());
            then(paymentRepository).should(never()).save(any(Payment.class));
        }
    }

    @Nested
    @DisplayName("토스 API 호출 실패")
    class TossApiFailures {

        @Test
        @DisplayName("토스 API 호출이 실패하면 예외가 발생하고 Redis 데이터는 삭제되지 않는다")
        void execute_withTossApiFailure_throwsExceptionAndKeepsRedisData() {
            // given
            ConfirmPaymentUseCase.Param param = new ConfirmPaymentUseCase.Param(
                    testOrderId, testPaymentKey, testAmount, testMemberId
            );

            RedisTempPaymentService.TempPaymentDto tempDto =
                    new RedisTempPaymentService.TempPaymentDto(testOrderId, testAmount, testMemberId);
            given(redisTempPaymentService.get(testOrderId)).willReturn(tempDto);

            given(tossPaymentsClient.confirmPayment(testPaymentKey, testOrderId, testAmount))
                    .willThrow(PaymentErrors.TOSS_API_ERROR.toException());

            // when & then
            assertThatThrownBy(() -> confirmPaymentUseCase.execute(param))
                    .isInstanceOf(PaymentException.class)
                    .hasMessageContaining("토스페이먼츠 API 호출에 실패했습니다");

            then(paymentRepository).should(never()).save(any(Payment.class));
            then(honeyWalletRepository).should(never()).findByMemberIdWithLock(anyLong());
            then(redisTempPaymentService).should(never()).delete(anyString());
        }
    }

    @Nested
    @DisplayName("Param 테스트")
    class ParamTest {

        @Test
        @DisplayName("Param 객체가 정상적으로 생성된다")
        void param_createsSuccessfully() {
            // when
            ConfirmPaymentUseCase.Param param = new ConfirmPaymentUseCase.Param(
                    testOrderId, testPaymentKey, testAmount, testMemberId
            );

            // then
            assertThat(param.getOrderId()).isEqualTo(testOrderId);
            assertThat(param.getPaymentKey()).isEqualTo(testPaymentKey);
            assertThat(param.getAmount()).isEqualTo(testAmount);
            assertThat(param.getMemberId()).isEqualTo(testMemberId);
        }
    }

    @Nested
    @DisplayName("Result 검증")
    class ResultValidation {

        @Test
        @DisplayName("정상 실행 시 Result에 올바른 값이 담긴다")
        void execute_returnsCorrectResultValues() {
            // given
            ConfirmPaymentUseCase.Param param = new ConfirmPaymentUseCase.Param(
                    testOrderId, testPaymentKey, testAmount, testMemberId
            );

            RedisTempPaymentService.TempPaymentDto tempDto =
                    new RedisTempPaymentService.TempPaymentDto(testOrderId, testAmount, testMemberId);
            given(redisTempPaymentService.get(testOrderId)).willReturn(tempDto);

            TossPaymentsClient.TossPaymentResponse tossResponse =
                    new TossPaymentsClient.TossPaymentResponse(
                            testPaymentKey, testOrderId, "DONE", testAmount,
                            "카드", "2024-01-01T00:00:00", "2024-01-01T00:00:01"
                    );
            given(tossPaymentsClient.confirmPayment(testPaymentKey, testOrderId, testAmount))
                    .willReturn(tossResponse);

            Payment mockPayment = mock(Payment.class);
            given(mockPayment.getPaymentId()).willReturn(999L);
            given(paymentRepository.save(any(Payment.class))).willReturn(mockPayment);

            HoneyWallet mockWallet = mock(HoneyWallet.class);
            given(mockWallet.getBalance()).willReturn(500000L);
            given(honeyWalletRepository.findByMemberIdWithLock(testMemberId)).willReturn(Optional.of(mockWallet));
            given(honeyWalletRepository.save(any(HoneyWallet.class))).willReturn(mockWallet);

            HoneyHistory mockHistory = mock(HoneyHistory.class);
            given(honeyHistoryRepository.save(any(HoneyHistory.class))).willReturn(mockHistory);

            // when
            ConfirmPaymentUseCase.Result result = confirmPaymentUseCase.execute(param);

            // then
            assertThat(result.getPaymentId()).isEqualTo("999");
            assertThat(result.getCurrentBalance()).isEqualTo(500000);
            assertThat(result.getPaymentKey()).isEqualTo(testPaymentKey);
        }
    }
}