package org.skypro.banking_service.serviceQuery;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepositoryImpl;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.TransactionSumCompareExecutor;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.argumets.TransactionCompareFourArgument;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransactionSumCompareExecutorTest {

    private static final UUID testUserId = UUID.randomUUID();
    private final String productType = "DEBIT";
    private final String transactionType = "DEPOSIT";
    private final List<String> listArguments = List.of(
            productType,
            transactionType,
            "<",
            "10000"
    );

    @Mock
    private UserTransactionRepositoryImpl userTransactionRepository;

    @InjectMocks
    private TransactionSumCompareExecutor transactionSumCompareExecutor;

    @Test
    void shouldReturnResultOfSupportsTrue() {
        String queryTest = "TRANSACTION_SUM_COMPARE";
        boolean result = transactionSumCompareExecutor.supports(queryTest);
        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfSupportsFalse() {
        String queryTest = "TRANSACTION_COMPARE";
        boolean result = transactionSumCompareExecutor.supports(queryTest);
        assertFalse(result);
    }

    @Test
    void shouldReturnResultOfEvaluateTrue() {
        when(userTransactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                testUserId,
                productType,
                transactionType
        )).thenReturn(20000L);

        boolean result = transactionSumCompareExecutor.evaluate(
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
                productType,
                transactionType
        )).thenReturn(5000L);

        boolean result = transactionSumCompareExecutor.evaluate(
                testUserId,
                listArguments,
                true
        );

        assertFalse(result);
    }
}