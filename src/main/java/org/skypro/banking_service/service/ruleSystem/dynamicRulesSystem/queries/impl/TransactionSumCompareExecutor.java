package org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.impl;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.parameter.TransactionCompareFourArgument;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.DynamicQueryExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.skypro.banking_service.constants.ConstantsForDynamicRules.TRANSACTION_SUM_COMPARE;

@Component
public class TransactionSumCompareExecutor implements DynamicQueryExecutor {

    private final UserTransactionRepository repository;

    public TransactionSumCompareExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Метод проверяет тип запроса, соответствует ли он поддерживаемому системой, в данном случае "TRANSACTION_SUM_COMPARE".
     *
     * @param queryType - тип запроса.
     * @return булевое значение: true - если соответствует, false - если нет.
     */
    @Override
    public boolean checkOutNameQuery(String queryType) {
        return TRANSACTION_SUM_COMPARE.equalsIgnoreCase(queryType);
    }

    /**
     * Метод сравнивает сумму всех транзакций типа Y по продуктам типа X с некоторой константой C.
     *
     * @param userId    - id клиента.
     * @param arguments - аргументы запроса.
     * @param negate    - модификатор отрицания.
     * @return булевое значение: true - если соответствует, false - если нет.
     */
    @Override
    public boolean checkOutQuery(UUID userId, List<String> arguments, boolean negate) {
        TransactionCompareFourArgument parsed = TransactionCompareFourArgument.from(arguments);

        long totalAmount = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                userId,
                parsed.productType(),
                parsed.transactionType());

        boolean result = parsed.operator().compare(totalAmount, parsed.value());
        return negate != result;
    }
}
