package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.payment.infrastructure.redis.RedisTempPaymentService;
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
 * PreparePaymentUseCase 단위 테스트
 *
 * 테스트 범위: 결제 준비 유스케이스 로직
 * Mock 대상: RedisTempPaymentService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PreparePaymentUseCase 단위 테스트")
class PreparePaymentUseCaseTest {

    @Mock
    private RedisTempPaymentService redisTempPaymentService;

    @InjectMocks
    private PreparePaymentUseCase preparePaymentUseCase;

    private Long testMemberId;
    private Integer testAmount;

    @BeforeEach
    void setUp() {
        testMemberId = 100L;
        testAmount = 10000;
    }

    @Nested
    @DisplayName("execute() 메서드")
    class ExecuteTest {

        @Test
        @DisplayName("정상적인 파라미터로 실행하면 orderId가 생성되고 Redis에 저장된다")
        void execute_withValidParams_createsOrderIdAndSavesToRedis() {
            // given
            String expectedOrderId = "0P2V16C5HW8MN";
            PreparePaymentUseCase.Param param = new PreparePaymentUseCase.Param(testMemberId, testAmount);

            given(redisTempPaymentService.save(testMemberId, testAmount)).willReturn(expectedOrderId);

            // when
            PreparePaymentUseCase.Result result = preparePaymentUseCase.execute(param);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getOrderId()).isEqualTo(expectedOrderId);
            assertThat(result.getAmount()).isEqualTo(testAmount);
            then(redisTempPaymentService).should().save(testMemberId, testAmount);
        }

        @Test
        @DisplayName("서로 다른 memberId로 실행하면 각각 다른 orderId가 생성된다")
        void execute_withDifferentMemberId_createsDifferentOrderId() {
            // given
            Long memberId1 = 100L;
            Long memberId2 = 200L;
            String orderId1 = "ORDER_001";
            String orderId2 = "ORDER_002";

            PreparePaymentUseCase.Param param1 = new PreparePaymentUseCase.Param(memberId1, testAmount);
            PreparePaymentUseCase.Param param2 = new PreparePaymentUseCase.Param(memberId2, testAmount);

            given(redisTempPaymentService.save(memberId1, testAmount)).willReturn(orderId1);
            given(redisTempPaymentService.save(memberId2, testAmount)).willReturn(orderId2);

            // when
            PreparePaymentUseCase.Result result1 = preparePaymentUseCase.execute(param1);
            PreparePaymentUseCase.Result result2 = preparePaymentUseCase.execute(param2);

            // then
            assertThat(result1.getOrderId()).isEqualTo(orderId1);
            assertThat(result2.getOrderId()).isEqualTo(orderId2);
            assertThat(result1.getOrderId()).isNotEqualTo(result2.getOrderId());
        }

        @Test
        @DisplayName("최소 금액(1,000원)으로 실행하면 정상적으로 처리된다")
        void execute_withMinimumAmount_succeeds() {
            // given
            Integer minAmount = 1000;
            String expectedOrderId = "MIN_ORDER_ID";
            PreparePaymentUseCase.Param param = new PreparePaymentUseCase.Param(testMemberId, minAmount);

            given(redisTempPaymentService.save(testMemberId, minAmount)).willReturn(expectedOrderId);

            // when
            PreparePaymentUseCase.Result result = preparePaymentUseCase.execute(param);

            // then
            assertThat(result.getAmount()).isEqualTo(minAmount);
            assertThat(result.getOrderId()).isEqualTo(expectedOrderId);
        }

        @Test
        @DisplayName("최대 금액(1,000,000원)으로 실행하면 정상적으로 처리된다")
        void execute_withMaximumAmount_succeeds() {
            // given
            Integer maxAmount = 1000000;
            String expectedOrderId = "MAX_ORDER_ID";
            PreparePaymentUseCase.Param param = new PreparePaymentUseCase.Param(testMemberId, maxAmount);

            given(redisTempPaymentService.save(testMemberId, maxAmount)).willReturn(expectedOrderId);

            // when
            PreparePaymentUseCase.Result result = preparePaymentUseCase.execute(param);

            // then
            assertThat(result.getAmount()).isEqualTo(maxAmount);
            assertThat(result.getOrderId()).isEqualTo(expectedOrderId);
        }
    }

    @Nested
    @DisplayName("Param 테스트")
    class ParamTest {

        @Test
        @DisplayName("Param 객체가 정상적으로 생성된다")
        void param_createsSuccessfully() {
            // when
            PreparePaymentUseCase.Param param = new PreparePaymentUseCase.Param(testMemberId, testAmount);

            // then
            assertThat(param.getMemberId()).isEqualTo(testMemberId);
            assertThat(param.getAmount()).isEqualTo(testAmount);
        }
    }

    @Nested
    @DisplayName("Result 검증")
    class ResultValidation {

        @Test
        @DisplayName("정상 실행 시 Result에 올바른 값이 담긴다")
        void execute_returnsCorrectResultValues() {
            // given
            String expectedOrderId = "VALIDATION_ORDER_ID";
            PreparePaymentUseCase.Param param = new PreparePaymentUseCase.Param(testMemberId, testAmount);

            given(redisTempPaymentService.save(testMemberId, testAmount)).willReturn(expectedOrderId);

            // when
            PreparePaymentUseCase.Result result = preparePaymentUseCase.execute(param);

            // then
            assertThat(result.getOrderId()).isEqualTo(expectedOrderId);
            assertThat(result.getAmount()).isEqualTo(testAmount);
        }
    }
}