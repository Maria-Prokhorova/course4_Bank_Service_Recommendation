package org.skypro.banking_service.repositories.h2.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserTransactionRepositoryImpl implements UserTransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserTransactionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> findUsedProductTypesByUserId(UUID userId) {
        String sql = """
                SELECT DISTINCT p.type
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ?
                """;
        return jdbcTemplate.queryForList(sql, String.class, userId);
        //Возвращает список всех типов продуктов, которыми уже пользовался user
    }

    @Override
    public boolean existsUserProductByType(UUID userId, String productType) {
        String sql = """
                SELECT COUNT(*) > 0
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ?
                  AND p.type = ?
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType));
        // Проверяем пользовался ли user инвест продуктом
    }

    @Override
    public boolean userExists(UUID userId) {
        String sql = """
                SELECT COUNT(*) > 0 
                FROM users 
                WHERE id = ?
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId));
        // Поверяет наличие пользователя с заданным userId
    }

    @Override
    public int countTransactionsByUserIdAndProductType(UUID userId, String productType) {
        String sql = """
            SELECT COUNT(*)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ?
              AND p.type = ?
            """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
        // Подсчитывает количество транзакций пользователя по заданному типу продукта
    }
    @Override
    public long findTotalAmountByUserIdAndProductTypeAndTransactionType(
            UUID userId, String productType, String transactionType) {
        TransactionQueryKey key = new TransactionQueryKey(userId, productType, transactionType);
        return cache.totalAmountCache.get(key, k -> {
            String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ?
              AND p.type = ?
              AND t.type = ?
            """;
            return jdbcTemplate.queryForObject(sql, Long.class, k.userId(), k.productType(), k.transactionType());
        });
        // Возвращает общую сумму транзакций пользователя по заданному типу продукта и типу транзакции.
    }
}
