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

    /**
     * Метод находит список продуктов используемый клиентом.
     *
     * @param userId - id клиента.
     * @return список найденных банковских продуктов.
     */
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

    /**
     * Метод проверяет использование клиентом заданного продукта.
     *
     * @param userId      - id клиента.
     * @param productType - тип банковского продукта.
     * @return булевое значение: true - если продукт использовался, false - если нет.
     */
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
    }

    /**
     * Метод поверяет наличие клиента с заданным Id.
     *
     * @param userId - id клиента.
     * @return булевое значение: true - если клиент в базе существует, false - если нет.
     */
    @Override
    public boolean userExists(UUID userId) {
        String sql = """
                SELECT COUNT(*) > 0
                FROM users
                WHERE id = ?
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId));
    }

    /**
     * Метод подсчитывает количество транзакций у клиента по заданному типу продукта.
     *
     * @param userId      - id клиента.
     * @param productType - тип банковского продукта.
     * @return количество транзакций.
     */
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
    }

    /**
     * Метод возвращает общую сумму транзакций клиента по заданному типу продукта и типу транзакции.
     *
     * @param userId          - id клиента.
     * @param productType     - тип банковского продукта.
     * @param transactionType - типу транзакции.
     * @return сумма транзакций.
     */
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
    }

    /**
     * Метод проверяет существование клиента в базе данных.
     *
     * @param username - никнейм клиента.
     * @return если клиент в БД существует, то вернет ID клиента,
     * если клиент в БД не найден или установлено больше одного совпадения никнейма клиента, то вернет Optional.empty().
     */
    @Override
    public Optional<UUID> findUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        List<UUID> result = jdbcTemplate.queryForList(sql, UUID.class, username);
        if (result.size() == 1) {
            return Optional.of(result.get(0));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Метод проверяет существование клиента в базе данных.
     *
     * @param username - никнейм клиента.
     * @return если клиент в БД существует, то фамилию и имя клиента,
     * * если клиент в БД не найден или установлено больше одного совпадения никнейма клиента, то вернет Optional.empty().
     */
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
