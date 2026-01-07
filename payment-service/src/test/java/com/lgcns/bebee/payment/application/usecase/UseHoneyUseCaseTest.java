package com.lgcns.bebee.payment.application.usecase;

import com.lgcns.bebee.common.exception.DomainException;
import com.lgcns.bebee.common.exception.InvalidParamException;
import com.lgcns.bebee.payment.common.exception.PaymentErrors;
import com.lgcns.bebee.payment.common.exception.PaymentInvalidParamErrors;
import com.lgcns.bebee.payment.domain.entity.HoneyEscrow;
import com.lgcns.bebee.payment.domain.entity.HoneyHistory;
import com.lgcns.bebee.payment.domain.entity.HoneyWallet;
import com.lgcns.bebee.payment.domain.entity.sync.PaymentMatchSync;
import com.lgcns.bebee.payment.domain.repository.HoneyEscrowRepository;
import com.lgcns.bebee.payment.domain.repository.HoneyHistoryRepository;
import com.lgcns.bebee.payment.domain.service.HoneyEscrowService;
import com.lgcns.bebee.payment.domain.service.HoneyWalletService;
import com.lgcns.bebee.payment.domain.service.MatchReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

/**
 * UseHoneyUseCase 단위 테스트
 *
 * 테스트 범위: 꿀 사용 및 에스크로 생성 유스케이스 로직
 * Mock 대상: HoneyWalletService, MatchReader, HoneyEscrowRepository, HoneyEscrowService, HoneyHistoryRepository
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UseHoneyUseCase 단위 테스트")
class UseHoneyUseCaseTest {

    @Mock
    private HoneyWalletService honeyWalletService;

    @Mock
    private MatchReader matchReader;

    @Mock
    private HoneyEscrowRepository honeyEscrowRepository;

    @Mock
    private HoneyEscrowService honeyEscrowService;

    @Mock
    private HoneyHistoryRepository honeyHistoryRepository;

    @InjectMocks
    private UseHoneyUseCase useHoneyUseCase;

    private Long testMemberId;
    private Long testMatchId;
    private Long testHelperId;
    private Long testUseHoney;
    private PaymentMatchSync mockMatch;
    private HoneyWallet mockWallet;

    @BeforeEach
    void setUp() {
        testMemberId = 100L;
        testMatchId = 1L;
        testHelperId = 200L;
        testUseHoney = 100L; // 100꿀

        mockMatch = mock(PaymentMatchSync.class);
        mockWallet = mock(HoneyWallet.class);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("정상적인 꿀 사용 플로우가 성공한다")
        void execute_withValidParams_succeeds() {
            // given
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, testUseHoney
            );

            Long currentHoney = 500L; // 500꿀 (5만원)
            Long amount = testUseHoney * 100; // 100꿀 = 10,000원

            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(currentHoney);
            given(matchReader.findById(testMatchId)).willReturn(mockMatch);
            given(mockMatch.getMatchId()).willReturn(testMatchId);
            given(mockMatch.getDisabledId()).willReturn(testMemberId);
            given(mockMatch.getHelperId()).willReturn(testHelperId);
            given(honeyEscrowService.existsByMatchId(testMatchId)).willReturn(true);
            given(honeyWalletService.findByMemberId(testMemberId)).willReturn(mockWallet);

            HoneyEscrow mockEscrow = mock(HoneyEscrow.class);
            given(honeyEscrowRepository.save(any(HoneyEscrow.class))).willReturn(mockEscrow);

            HoneyHistory mockHistory = mock(HoneyHistory.class);
            given(honeyHistoryRepository.save(any(HoneyHistory.class))).willReturn(mockHistory);

            // when
            Void result = useHoneyUseCase.execute(param);

            // then
            assertThat(result).isNull();
            then(honeyWalletService).should().getCurrentHoney(testMemberId);
            then(matchReader).should().findById(testMatchId);
            then(honeyEscrowService).should().existsByMatchId(testMatchId);
            then(honeyEscrowRepository).should().save(any(HoneyEscrow.class));
            then(mockWallet).should().withdraw(amount);
            then(honeyHistoryRepository).should().save(any(HoneyHistory.class));
        }

        @Test
        @DisplayName("정확히 일치하는 잔액으로 꿀 사용에 성공한다")
        void execute_withExactBalance_succeeds() {
            // given
            Long exactHoney = testUseHoney; // 정확히 100꿀
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, testUseHoney
            );

            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(exactHoney);
            given(matchReader.findById(testMatchId)).willReturn(mockMatch);
            given(mockMatch.getMatchId()).willReturn(testMatchId);
            given(mockMatch.getDisabledId()).willReturn(testMemberId);
            given(mockMatch.getHelperId()).willReturn(testHelperId);
            given(honeyEscrowService.existsByMatchId(testMatchId)).willReturn(true);
            given(honeyWalletService.findByMemberId(testMemberId)).willReturn(mockWallet);
            given(honeyEscrowRepository.save(any(HoneyEscrow.class))).willReturn(mock(HoneyEscrow.class));
            given(honeyHistoryRepository.save(any(HoneyHistory.class))).willReturn(mock(HoneyHistory.class));

            // when
            Void result = useHoneyUseCase.execute(param);

            // then
            assertThat(result).isNull();
            then(mockWallet).should().withdraw(testUseHoney * 100);
        }
    }

    @Nested
    @DisplayName("잔액 부족")
    class InsufficientBalance {

        @Test
        @DisplayName("잔액이 부족하면 INSUFFICIENT_HONEY_BALANCE 예외를 던진다")
        void execute_withInsufficientBalance_throwsException() {
            // given
            Long insufficientHoney = 50L; // 50꿀 (필요: 100꿀)
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, testUseHoney
            );

            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(insufficientHoney);

            // when & then
            assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining(PaymentErrors.INSUFFICIENT_HONEY_BALANCE.getDesc());

            then(matchReader).should(never()).findById(anyLong());
            then(honeyEscrowRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("잔액이 0이면 예외를 던진다")
        void execute_withZeroBalance_throwsException() {
            // given
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, testUseHoney
            );

            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(0L);

            // when & then
            assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining(PaymentErrors.INSUFFICIENT_HONEY_BALANCE.getDesc());
        }
    }

    @Nested
    @DisplayName("매치 검증 실패")
    class MatchValidationFailures {

        @Test
        @DisplayName("매치를 찾을 수 없으면 MATCH_NOT_FOUND 예외를 던진다")
        void execute_withNonExistentMatch_throwsException() {
            // given
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, testUseHoney
            );

            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(1000L);
            given(matchReader.findById(testMatchId))
                    .willThrow(PaymentErrors.MATCH_NOT_FOUND.toException());

            // when & then
            assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining(PaymentErrors.MATCH_NOT_FOUND.getDesc());

            then(honeyEscrowRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("결제 대상 회원이 아니면 PAYMENT_MEMBER_MISMATCH 예외를 던진다")
        void execute_withWrongMember_throwsException() {
            // given
            Long wrongMemberId = 999L;
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, testUseHoney
            );

            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(1000L);
            given(matchReader.findById(testMatchId)).willReturn(mockMatch);
            given(mockMatch.getDisabledId()).willReturn(wrongMemberId); // 다른 회원

            // when & then
            assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining(PaymentErrors.PAYMENT_MEMBER_MISMATCH.getDesc());

            then(honeyEscrowService).should(never()).existsByMatchId(anyLong());
            then(honeyEscrowRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("에스크로 검증 실패")
    class EscrowValidationFailures {

        @Test
        @DisplayName("이미 에스크로가 존재하면 ESCROW_ALREADY_EXISTS 예외를 던진다")
        void execute_withExistingEscrow_throwsException() {
            // given
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, testUseHoney
            );

            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(1000L);
            given(matchReader.findById(testMatchId)).willReturn(mockMatch);
            given(mockMatch.getMatchId()).willReturn(testMatchId);
            given(mockMatch.getDisabledId()).willReturn(testMemberId);
            given(honeyEscrowService.existsByMatchId(testMatchId))
                    .willThrow(PaymentErrors.ESCROW_ALREADY_EXISTS.toException());

            // when & then
            assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining(PaymentErrors.ESCROW_ALREADY_EXISTS.getDesc());

            then(honeyEscrowRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("Param 검증")
    class ParamValidation {

        @Test
        @DisplayName("useHoney가 null이면 REQUIRED_FIELD 예외를 던진다")
        void validate_withNullUseHoney_throwsException() {
            // given
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, null
            );

            // when & then
            assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining(PaymentInvalidParamErrors.REQUIRED_FIELD.getDesc())
                    .hasMessageContaining("useHoney");

            then(honeyWalletService).should(never()).getCurrentHoney(anyLong());
        }

        @Test
        @DisplayName("useHoney가 0이면 HONEY_USE_MUST_BE_POSITIVE 예외를 던진다")
        void validate_withZeroUseHoney_throwsException() {
            // given
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, 0L
            );

            // when & then
            assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining(PaymentInvalidParamErrors.HONEY_USE_MUST_BE_POSITIVE.getDesc());

            then(honeyWalletService).should(never()).getCurrentHoney(anyLong());
        }

        @Test
        @DisplayName("useHoney가 음수이면 HONEY_USE_MUST_BE_POSITIVE 예외를 던진다")
        void validate_withNegativeUseHoney_throwsException() {
            // given
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, -100L
            );

            // when & then
            assertThatThrownBy(() -> useHoneyUseCase.execute(param))
                    .isInstanceOf(InvalidParamException.class)
                    .hasMessageContaining(PaymentInvalidParamErrors.HONEY_USE_MUST_BE_POSITIVE.getDesc());

            then(honeyWalletService).should(never()).getCurrentHoney(anyLong());
        }

        @Test
        @DisplayName("Param 객체가 정상적으로 생성된다")
        void param_createsSuccessfully() {
            // when
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, testUseHoney
            );

            // then
            assertThat(param.getMemberId()).isEqualTo(testMemberId);
            assertThat(param.getMatchId()).isEqualTo(testMatchId);
            assertThat(param.getUseHoney()).isEqualTo(testUseHoney);
        }
    }

    @Nested
    @DisplayName("엣지 케이스")
    class EdgeCases {

        @Test
        @DisplayName("매우 큰 금액의 꿀 사용도 정상 처리된다")
        void execute_withLargeAmount_succeeds() {
            // given
            Long largeUseHoney = 1000000L; // 100만 꿀 = 1억원
            Long largeHoney = 2000000L; // 200만 꿀 = 2억원
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, largeUseHoney
            );

            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(largeHoney);
            given(matchReader.findById(testMatchId)).willReturn(mockMatch);
            given(mockMatch.getMatchId()).willReturn(testMatchId);
            given(mockMatch.getDisabledId()).willReturn(testMemberId);
            given(mockMatch.getHelperId()).willReturn(testHelperId);
            given(honeyEscrowService.existsByMatchId(testMatchId)).willReturn(true);
            given(honeyWalletService.findByMemberId(testMemberId)).willReturn(mockWallet);
            given(honeyEscrowRepository.save(any(HoneyEscrow.class))).willReturn(mock(HoneyEscrow.class));
            given(honeyHistoryRepository.save(any(HoneyHistory.class))).willReturn(mock(HoneyHistory.class));

            // when
            Void result = useHoneyUseCase.execute(param);

            // then
            assertThat(result).isNull();
            then(mockWallet).should().withdraw(largeUseHoney * 100);
        }

        @Test
        @DisplayName("1개의 꿀만 사용하는 경우도 정상 처리된다")
        void execute_withSingleHoney_succeeds() {
            // given
            Long singleHoney = 1L; // 1꿀 = 100원
            UseHoneyUseCase.Param param = new UseHoneyUseCase.Param(
                    testMemberId, testMatchId, singleHoney
            );

            given(honeyWalletService.getCurrentHoney(testMemberId)).willReturn(1000L);
            given(matchReader.findById(testMatchId)).willReturn(mockMatch);
            given(mockMatch.getMatchId()).willReturn(testMatchId);
            given(mockMatch.getDisabledId()).willReturn(testMemberId);
            given(mockMatch.getHelperId()).willReturn(testHelperId);
            given(honeyEscrowService.existsByMatchId(testMatchId)).willReturn(true);
            given(honeyWalletService.findByMemberId(testMemberId)).willReturn(mockWallet);
            given(honeyEscrowRepository.save(any(HoneyEscrow.class))).willReturn(mock(HoneyEscrow.class));
            given(honeyHistoryRepository.save(any(HoneyHistory.class))).willReturn(mock(HoneyHistory.class));

            // when
            Void result = useHoneyUseCase.execute(param);

            // then
            assertThat(result).isNull();
            then(mockWallet).should().withdraw(100L);
        }
    }
}