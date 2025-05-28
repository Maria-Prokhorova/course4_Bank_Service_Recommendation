package org.skypro.banking_service.service.dynamicRuleSystem.queries;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepositoryImpl;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.TransactionSumCompareExecutor;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
    void shouldReturnResultOfCheckOutNameQueryTrue() {
        String queryTest = "TRANSACTION_SUM_COMPARE";
        boolean result = transactionSumCompareExecutor.checkOutNameQuery(queryTest);
        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfCheckOutNameQueryFalse() {
        String queryTest = "TRANSACTION_COMPARE";
        boolean result = transactionSumCompareExecutor.checkOutNameQuery(queryTest);
        assertFalse(result);
    }

    @Test
    void shouldReturnResultOfCheckOutQueryTrue() {
        when(userTransactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                testUserId,
                productType,
                transactionType
        )).thenReturn(20000L);

        boolean result = transactionSumCompareExecutor.checkOutQuery(
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
                productType,
                transactionType
        )).thenReturn(5000L);

        boolean result = transactionSumCompareExecutor.checkOutQuery(
                testUserId,
                listArguments,
                true
        );

        assertFalse(result);
    }
}