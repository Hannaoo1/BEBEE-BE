package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import com.lgcns.bebee.payment.common.exception.PaymentInvalidParamErrors;
import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import com.lgcns.bebee.payment.domain.service.HoneyWalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UseHoneyUseCase 테스트")
class UseHoneyUseCaseTest {

    @Mock
    private HoneyWalletService honeyWalletService;

    @InjectMocks
    private UseHoneyUseCase useHoneyUseCase;

    private Long testMemberId;
    private HoneyWallet mockWallet;

    @BeforeEach
    void setUp() {
        testMemberId = 1L;
        mockWallet = mock(HoneyWallet.class);
    }

    @Test
    @DisplayName("충분한 잔액이 있으면 꿀 사용에 성공한다")
    void execute_withSufficientBalance_succeeds() {
        // given
        Long useHoney = 100L; // 100꿀 사용
        Long currentHoney = 500L; // 현재 500꿀 보유

        given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(currentHoney);
        given(honeyWalletService.findByMemberId(testMemberId)).willReturn(mockWallet);

        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(testMemberId, useHoney);

        // when
        Void result = useHoneyUseCase.execute(param);

        // then
        assertThat(result).isNull();
        verify(honeyWalletService).getCurrentHoney(testMemberId);
        verify(honeyWalletService).findByMemberId(testMemberId);
        verify(mockWallet).withdraw(useHoney);
    }

    @Test
    @DisplayName("정확히 일치하는 잔액이 있으면 꿀 사용에 성공한다")
    void execute_withExactBalance_succeeds() {
        // given
        Long useHoney = 100L; // 100꿀 사용
        Long currentHoney = 100L; // 현재 100꿀 보유 (정확히 일치)

        given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(currentHoney);
        given(honeyWalletService.findByMemberId(testMemberId)).willReturn(mockWallet);

        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(testMemberId, useHoney);

        // when
        Void result = useHoneyUseCase.execute(param);

        // then
        assertThat(result).isNull();
        verify(honeyWalletService).getCurrentHoney(testMemberId);
        verify(honeyWalletService).findByMemberId(testMemberId);
        verify(mockWallet).withdraw(useHoney);
    }

    @Test
    @DisplayName("잔액이 부족하면 INSUFFICIENT_HONEY_BALANCE 예외를 던진다")
    void execute_withInsufficientBalance_throwsException() {
        // given
        Long useHoney = 100L; // 100꿀 사용 시도
        Long currentHoney = 50L; // 현재 50꿀만 보유

        given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(currentHoney);

        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(testMemberId, useHoney);

        // when & then
        assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(PaymentErrors.INSUFFICIENT_HONEY_BALANCE.getDesc());

        verify(honeyWalletService).getCurrentHoney(testMemberId);
        verify(honeyWalletService, never()).findByMemberId(any());
        verify(mockWallet, never()).withdraw(any());
    }

    @Test
    @DisplayName("잔액이 0이고 꿀을 사용하려고 하면 예외를 던진다")
    void execute_withZeroBalance_throwsException() {
        // given
        Long useHoney = 10L;
        Long currentHoney = 0L;

        given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(currentHoney);

        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(testMemberId, useHoney);

        // when & then
        assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(PaymentErrors.INSUFFICIENT_HONEY_BALANCE.getDesc());
    }

    @Test
    @DisplayName("지갑이 존재하지 않으면 HONEY_WALLET_NOT_FOUND 예외를 던진다")
    void execute_withNonExistentWallet_throwsException() {
        // given
        Long useHoney = 100L;
        Long currentHoney = 500L;

        given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(currentHoney);
        given(honeyWalletService.findByMemberId(testMemberId))
                .willThrow(PaymentErrors.HONEY_WALLET_NOT_FOUND.toException());

        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(testMemberId, useHoney);

        // when & then
        assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining(PaymentErrors.HONEY_WALLET_NOT_FOUND.getDesc());

        verify(honeyWalletService).getCurrentHoney(testMemberId);
        verify(honeyWalletService).findByMemberId(testMemberId);
        verify(mockWallet, never()).withdraw(any());
    }

    @Test
    @DisplayName("useHoney가 null이면 REQUIRED_FIELD 예외를 던진다")
    void validate_withNullUseHoney_throwsException() {
        // given
        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(testMemberId, null);

        // when & then
        assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                .isInstanceOf(InvalidParamException.class)
                .hasMessageContaining(PaymentInvalidParamErrors.REQUIRED_FIELD.getDesc())
                .hasMessageContaining("useHoney");

        verify(honeyWalletService, never()).getCurrentHoney(any());
        verify(honeyWalletService, never()).findByMemberId(any());
    }

    @Test
    @DisplayName("useHoney가 0이면 HONEY_USE_MUST_BE_POSITIVE 예외를 던진다")
    void validate_withZeroUseHoney_throwsException() {
        // given
        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(testMemberId, 0L);

        // when & then
        assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                .isInstanceOf(InvalidParamException.class)
                .hasMessageContaining(PaymentInvalidParamErrors.HONEY_USE_MUST_BE_POSITIVE.getDesc());

        verify(honeyWalletService, never()).getCurrentHoney(any());
        verify(honeyWalletService, never()).findByMemberId(any());
    }

    @Test
    @DisplayName("useHoney가 음수이면 HONEY_USE_MUST_BE_POSITIVE 예외를 던진다")
    void validate_withNegativeUseHoney_throwsException() {
        // given
        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(testMemberId, -100L);

        // when & then
        assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                .isInstanceOf(InvalidParamException.class)
                .hasMessageContaining(PaymentInvalidParamErrors.HONEY_USE_MUST_BE_POSITIVE.getDesc());

        verify(honeyWalletService, never()).getCurrentHoney(any());
        verify(honeyWalletService, never()).findByMemberId(any());
    }

    @Test
    @DisplayName("매우 큰 금액의 꿀 사용도 정상 처리된다")
    void execute_withLargeAmount_succeeds() {
        // given
        Long useHoney = 1000000L; // 100만 꿀 사용
        Long currentHoney = 2000000L; // 200만 꿀 보유

        given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(currentHoney);
        given(honeyWalletService.findByMemberId(testMemberId)).willReturn(mockWallet);

        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(testMemberId, useHoney);

        // when
        Void result = useHoneyUseCase.execute(param);

        // then
        assertThat(result).isNull();
        verify(honeyWalletService).getCurrentHoney(testMemberId);
        verify(honeyWalletService).findByMemberId(testMemberId);
        verify(mockWallet).withdraw(useHoney);
    }

    @Test
    @DisplayName("1개의 꿀만 사용하는 경우도 정상 처리된다")
    void execute_withSingleHoney_succeeds() {
        // given
        Long useHoney = 1L; // 1꿀 사용
        Long currentHoney = 10L; // 10꿀 보유

        given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(currentHoney);
        given(honeyWalletService.findByMemberId(testMemberId)).willReturn(mockWallet);

        UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(testMemberId, useHoney);

        // when
        Void result = useHoneyUseCase.execute(param);

        // then
        assertThat(result).isNull();
        verify(honeyWalletService).getCurrentHoney(testMemberId);
        verify(honeyWalletService).findByMemberId(testMemberId);
        verify(mockWallet).withdraw(useHoney);
    }
}