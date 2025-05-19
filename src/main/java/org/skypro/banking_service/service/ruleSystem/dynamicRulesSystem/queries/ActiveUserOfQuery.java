package org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.skypro.banking_service.constants.ConstantsForDynamicRules.*;

@Component
public class ActiveUserOfQuery implements DimanicQueryExecutor {

    private final UserTransactionRepository repository;

    public ActiveUserOfQuery(UserTransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Метод проверяет тип запроса, соответствует ли он поддерживаемому системой, в данном случае "ACTIVE_USER_OF".
     *
     * @param queryType - тип запроса.
     * @return булевое значение: true - если соответствует, false - если нет.
     */
    @Override
    public boolean checkOutNameQuery(String queryType) {
        return ACTIVE_USER_OF.equalsIgnoreCase(queryType);
    }

    /**
     * Метод проверяет, является ли клиент, активным пользователем продукта X (есть 5 транзакций).
     *
     * @param userId    - id клиента.
     * @param arguments - аргументы запроса.
     * @param negate    - модификатор отрицания.
     * @return булевое значение: true - если соответствует, false - если нет.
     */
    @Override
    public boolean checkOutQuery(UUID userId, List<String> arguments, boolean negate) {
        String productType = arguments.get(0);
        boolean result = repository.countTransactionsByUserIdAndProductType(
                userId, productType) >= MIN_TRANSACTIONS_FOR_ACTIVE_USER;
        return negate != result;
    }
}

