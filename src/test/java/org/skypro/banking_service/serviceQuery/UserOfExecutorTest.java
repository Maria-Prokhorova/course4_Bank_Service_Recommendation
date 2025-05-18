package org.skypro.banking_service.serviceQuery;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepositoryImpl;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.UserOfExecutor;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserOfExecutorTest {

    private static final UUID testUserId = UUID.randomUUID();
    private final String productType = "DEBIT";
    private final List<String> listArguments = List.of(productType);

    @Mock
    private UserTransactionRepositoryImpl userRepository;

    @InjectMocks
    private UserOfExecutor userOfExecutor;

    @Test
    void shouldReturnResultOfSupportsTrue() {
        String queryTest = "USER_OF";
        boolean result = userOfExecutor.supports(queryTest);
        assertTrue(result);
    }

    @Test
    void shouldReturnResultOfSupportsFalse() {
        String queryTest = "USER_ON";
        boolean result = userOfExecutor.supports(queryTest);
        assertFalse(result);
    }

    @Test
    void shouldReturnUserOfEvaluateTrue() {
        when(userRepository.countTransactionsByUserIdAndProductType(
                testUserId,
                productType
        )).thenReturn(0);

        boolean result = userOfExecutor.evaluate(testUserId, listArguments, true);
        assertTrue(result);
    }

    @Test
    void shouldReturnUserOfEvaluateFalse() {
        when(userRepository.countTransactionsByUserIdAndProductType(
                testUserId,
                productType
        )).thenReturn(2);

        boolean result = userOfExecutor.evaluate(testUserId, listArguments, true);
        assertFalse(result);
    }
}