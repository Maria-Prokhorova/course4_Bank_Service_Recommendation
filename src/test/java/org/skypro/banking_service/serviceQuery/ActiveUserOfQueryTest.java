package org.skypro.banking_service.serviceQuery;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepositoryImpl;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.ActiveUserOfQuery;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ActiveUserOfQueryTest {

    private static final UUID testUserId = UUID.randomUUID();
    private final String productType = "DEBIT";
    private final List<String> listProduct = List.of(productType);

    @Mock
    private UserTransactionRepositoryImpl userTransactionRepository;

    @InjectMocks
    private ActiveUserOfQuery activeUserOfQuery;

    @Test
    void shouldReturnResultOfSupportsTrue() {
        String queryTest = "active_user_of";
        boolean result = activeUserOfQuery.supports(queryTest);
        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfSupportsFalse() {
        String queryTest = "active_user";
        boolean result = activeUserOfQuery.supports(queryTest);
        assertFalse(result);
    }

    @Test
    void shouldReturnResultOfEvaluateTrue() {
        when(userTransactionRepository.countTransactionsByUserIdAndProductType(
                eq(testUserId),
                eq(productType)
        )).thenReturn(1);

        boolean result = activeUserOfQuery.evaluate(
                testUserId,
                listProduct,
                true
        );

        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfEvaluateFalse() {
        when(userTransactionRepository.countTransactionsByUserIdAndProductType(
                eq(testUserId),
                eq(productType)
        )).thenReturn(7);

        boolean result = activeUserOfQuery.evaluate(
                testUserId,
                listProduct,
                true
        );

        assertFalse(result);
    }
}