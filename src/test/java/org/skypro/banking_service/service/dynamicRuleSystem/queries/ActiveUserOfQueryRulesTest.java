package org.skypro.banking_service.service.dynamicRuleSystem.queries;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepositoryImpl;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.impl.ActiveUserOfQuery;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActiveUserOfQueryRulesTest {

    private static final UUID testUserId = UUID.randomUUID();
    private final String productType = "DEBIT";
    private final List<String> listProduct = List.of(productType);

    @Mock
    private UserTransactionRepositoryImpl userTransactionRepository;

    @InjectMocks
    private ActiveUserOfQuery activeUserOfQuery;

    @Test
    void shouldReturnResultOfCheckOutNameQueryTrue() {
        String queryTest = "active_user_of";
        boolean result = activeUserOfQuery.checkOutNameQuery(queryTest);
        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfCheckOutNameQueryFalse() {
        String queryTest = "active_user";
        boolean result = activeUserOfQuery.checkOutNameQuery(queryTest);
        assertFalse(result);
    }

    @Test
    void shouldReturnResultOfCheckOutQueryTrue() {
        when(userTransactionRepository.countTransactionsByUserIdAndProductType(
                eq(testUserId),
                eq(productType)
        )).thenReturn(1);

        boolean result = activeUserOfQuery.checkOutQuery(
                testUserId,
                listProduct,
                true
        );

        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfCheckOutQueryFalse() {
        when(userTransactionRepository.countTransactionsByUserIdAndProductType(
                eq(testUserId),
                eq(productType)
        )).thenReturn(7);

        boolean result = activeUserOfQuery.checkOutQuery(
                testUserId,
                listProduct,
                true
        );

        assertFalse(result);
    }
}