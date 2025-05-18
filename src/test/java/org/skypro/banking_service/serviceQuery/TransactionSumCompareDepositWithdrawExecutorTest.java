package org.skypro.banking_service.serviceQuery;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepositoryImpl;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.TransactionSumCompareDepositWithdrawExecutor;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.argumets.TransactionCompareTwoArguments;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.enums.Operator;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.skypro.banking_service.constants.TransactionTypeConstants.DEPOSIT;
import static org.skypro.banking_service.constants.TransactionTypeConstants.WITHDRAW;
import static org.skypro.banking_service.ruleSystem.dynamicRulesSystem.enums.Operator.LT;

@SpringBootTest
public class TransactionSumCompareDepositWithdrawExecutorTest {

    private static final UUID testUserId = UUID.randomUUID();
    private final String productTypeFirst = "DEBIT";
    private final Operator operatorTypeSecond = LT;
    private final List<String> listArguments = List.of(productTypeFirst, "<");

    @Mock
    private UserTransactionRepositoryImpl userTransactionRepository;

    @InjectMocks
    private TransactionSumCompareDepositWithdrawExecutor transactionSumCompareDepositWithdrawExecutor;

    @Test
    void shouldReturnResultOfSupportsTrue() {
        String queryTest = "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW";
        boolean result = transactionSumCompareDepositWithdrawExecutor.supports(queryTest);
        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfSupportsFalse() {
        String queryTest = "COMPARE_DEPOSIT_WITHDRAW";
        boolean result = transactionSumCompareDepositWithdrawExecutor.supports(queryTest);
        assertFalse(result);
    }

    @Test
    void shouldReturnResultOfEvaluateTrue() {
        when(userTransactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                testUserId,
                productTypeFirst,
                DEPOSIT
        )).thenReturn(20000L);

        when(userTransactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                testUserId,
                productTypeFirst,
                WITHDRAW
        )).thenReturn(10000L);

        boolean result = transactionSumCompareDepositWithdrawExecutor.evaluate(
                testUserId,
                listArguments,
                true
        );

        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfEvaluateFalse() {
        when(userTransactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                testUserId,
                productTypeFirst,
                DEPOSIT
        )).thenReturn(10000L);

        when(userTransactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                testUserId,
                productTypeFirst,
                WITHDRAW
        )).thenReturn(20000L);

        boolean result = transactionSumCompareDepositWithdrawExecutor.evaluate(
                testUserId,
                listArguments,
                true
        );

        assertFalse(result);
    }
}
