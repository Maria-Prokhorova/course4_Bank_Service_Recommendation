package org.skypro.banking_service.serviceQuery;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.skypro.banking_service.constants.ConstantsForDynamicRules;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepositoryImpl;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.TransactionSumCompareDepositWithdrawExecutor;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.enums.Operator;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.enums.Operator.LT;

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
    void shouldReturnResultOfCheckOutNameQueryTrue() {
        String queryTest = "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW";
        boolean result = transactionSumCompareDepositWithdrawExecutor.checkOutNameQuery(queryTest);
        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfCheckOutNameQueryFalse() {
        String queryTest = "COMPARE_DEPOSIT_WITHDRAW";
        boolean result = transactionSumCompareDepositWithdrawExecutor.checkOutNameQuery(queryTest);
        assertFalse(result);
    }

    @Test
    void shouldReturnResultOfCheckOutQueryTrue() {
        when(userTransactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                testUserId,
                productTypeFirst,
                String.valueOf(ConstantsForDynamicRules.TypeTransaction.DEPOSIT)
        )).thenReturn(20000L);

        when(userTransactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                testUserId,
                productTypeFirst,
                String.valueOf(ConstantsForDynamicRules.TypeTransaction.WITHDRAW)
        )).thenReturn(10000L);

        boolean result = transactionSumCompareDepositWithdrawExecutor.checkOutQuery(
                testUserId,
                listArguments,
                true
        );

        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfCheckOutQueryFalse() {
        when(userTransactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                testUserId,
                productTypeFirst,
                String.valueOf(ConstantsForDynamicRules.TypeTransaction.DEPOSIT)
        )).thenReturn(10000L);

        when(userTransactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                testUserId,
                productTypeFirst,
                String.valueOf(ConstantsForDynamicRules.TypeTransaction.WITHDRAW)
        )).thenReturn(20000L);

        boolean result = transactionSumCompareDepositWithdrawExecutor.checkOutQuery(
                testUserId,
                listArguments,
                true
        );

        assertFalse(result);
    }
}
