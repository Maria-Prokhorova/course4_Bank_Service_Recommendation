package org.skypro.banking_service.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.banking_service.cache.TransactionQueryKey;
import org.skypro.banking_service.cache.UserProductKey;
import org.skypro.banking_service.cache.UserTransactionCache;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepositoryImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTransactionRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private UserTransactionCache cache;

    @InjectMocks
    private UserTransactionRepositoryImpl repository;

    private final UUID testUserId = UUID.randomUUID();
    private final String productType = "DEBIT";
    private final int countTransactions = 10;
    private final String transactionType = "DEPOSIT";
    private final long sumTransactions = 10000L;

    @BeforeEach
    void setUp() {
        // Создаем реальный экземпляр кэша
        cache = new UserTransactionCache();

        // Инициализируем репозиторий с реальным кэшем и моком JdbcTemplate
        repository = new UserTransactionRepositoryImpl(jdbcTemplate, cache);
    }

    @Test
    void existsUserProductByType_shouldReturnTrueAndCacheResult() {
        // мокирование метода объекта
        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Boolean.class),
                eq(testUserId),
                eq(productType))
        ).thenReturn(true);

        // Действие - первый вызов (должен пойти в БД)
        boolean firstResult = repository.existsUserProductByType(testUserId, productType);

        // Проверка результата вызова
        assertTrue(firstResult);
/*        verify(jdbcTemplate).queryForObject(
                anyString(),
                eq(Boolean.class),
                eq(testUserId),
                eq(productType)
        );*/

        // Проверка, что результат закэшировался
        UserProductKey cacheKey = new UserProductKey(testUserId, productType);
        assertNotNull(cache.userProductExsistsCache.getIfPresent(cacheKey));
        assertEquals(Boolean.TRUE, cache.userProductExsistsCache.getIfPresent(cacheKey));

        // Действие - второй вызов (должен взять из кэша)
        boolean secondResult = repository.existsUserProductByType(testUserId, productType);

        // Проверка - не должно быть новых вызовов к БД (не должен быть задействован мок JdbcTemplate)
        assertTrue(secondResult);
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void shouldReturnResultFindUsedProductTypesByUserIdFromDataBaseAndCacheResult() {
        // Проверка вовзрата результата списка продуктов пользователя через БД
        // + проверка работоспособности кеша
        List<String> expectedTypes = List.of("CREDIT", "DEBIT");
        when(jdbcTemplate.queryForList(
                anyString(),
                eq(String.class),
                eq(testUserId))
        ).thenReturn(expectedTypes);

        // Действие - первый вызов через БД
        List<String> firstResult = repository.findUsedProductTypesByUserId(testUserId);

        // Проверка результата вызова
        assertEquals(expectedTypes, firstResult);
/*        verify(jdbcTemplate).queryForList(
                anyString(),
                eq(String.class),
                eq(testUserId)
        );*/

        // Проверяем выполнение кэширования
        List<String> result = cache.userTypeProductCache.getIfPresent(testUserId);
        assertEquals(expectedTypes, result);

        // Действие - второй вызов через кэш
        List<String> secondResult = repository.findUsedProductTypesByUserId(testUserId);

        // Проверка - вызов из кэша, без запроса к БД
        assertEquals(expectedTypes, secondResult);
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void shouldReturnTrueIfUserExists() {
        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Boolean.class),
                eq(testUserId)))
                .thenReturn(true);

        boolean result = repository.userExists(testUserId);
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfUserExists() {
        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Boolean.class),
                eq(testUserId)))
                .thenReturn(false);

        boolean result = repository.userExists(testUserId);
        assertFalse(result);
    }

    @Test
    void shouldReturnCountTransactionsByUserIdAndProductType() {
        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Integer.class),
                eq(testUserId),
                eq(productType)))
                .thenReturn(countTransactions);

        int result = repository.countTransactionsByUserIdAndProductType(testUserId, productType);
        assertEquals(countTransactions, result);
    }

    @Test
    void shouldReturnFindTotalAmountByUserIdAndProductTypeAndTransactionTypeAndCacheResult() {
        when(jdbcTemplate.queryForObject(
                anyString(),
                eq(Long.class),
                eq(testUserId),
                eq(productType),
                eq(transactionType)))
                .thenReturn(sumTransactions);

        //Действие - вызов метода через БД + проверка
        Long firstResult = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(testUserId, productType, transactionType);
        assertEquals(sumTransactions, firstResult);

        // Проверка, что результат закэшировался
        TransactionQueryKey key = new TransactionQueryKey(testUserId, productType, transactionType);
        assertNotNull(cache.totalAmountCache.getIfPresent(key));

        // Действие - второй вызов (должен взять из кэша)
        Long secondResult = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(testUserId, productType, transactionType);;

        // Проверка - не должно быть новых вызовов к БД (не должен быть задействован мок JdbcTemplate)
        assertEquals(sumTransactions, secondResult);
        verifyNoMoreInteractions(jdbcTemplate);
    }
}