package org.skypro.banking_service.repositories.h2.repository;

import org.skypro.banking_service.cache.TransactionQueryKey;
import org.skypro.banking_service.cache.UserProductKey;
import org.skypro.banking_service.cache.UserTransactionCache;
import org.skypro.banking_service.telegramBot.dto.UserFullName;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserTransactionRepositoryImpl implements UserTransactionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserTransactionCache cache;

    public UserTransactionRepositoryImpl(JdbcTemplate jdbcTemplate, UserTransactionCache cache) {
        this.jdbcTemplate = jdbcTemplate;
        this.cache = cache;
    }

    @Override
    public List<String> findUsedProductTypesByUserId(UUID userId) {
        return cache.userTypeProductCache.get(userId, id -> {
            String sql = """
                SELECT DISTINCT p.type
                FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ?
                """;
            return jdbcTemplate.queryForList(sql, String.class, id);
        });
    }

    @Override
    public boolean existsUserProductByType(UUID userId, String productType) {
        UserProductKey key = new UserProductKey(userId, productType);
        return cache.userProductExsistsCache.get(key, k -> {
            String sql = """
                    SELECT COUNT(*) > 0
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ?
                      AND p.type = ?
                    """;
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId,
                    productType));
        });
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

    @Override
    public Optional<UUID> findUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        List<UUID> result = jdbcTemplate.queryForList(sql, UUID.class, username);
        if (result.size() == 1) {
            return Optional.of(result.get(0));
        } else {
            return Optional.empty();
        }
        // вернёт Optional.empty() если пользователь не найден или найдено больше одного.
    }

    @Override
    public Optional<UserFullName> findUserFullNameByUsername(String username) {
        String sql = """
        SELECT id, first_name, last_name
        FROM users
        WHERE username = ?
        """;

        List<UserFullName> users = jdbcTemplate.query(sql, (rs, rowNum) ->
                new UserFullName(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ), username);

        return users.size() == 1 ? Optional.of(users.get(0)) : Optional.empty();
    }
}
