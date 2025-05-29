package org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.impl;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.DynamicQueryExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.skypro.banking_service.constants.ConstantsForDynamicRules.*;

@Component
public class UserOfExecutor implements DynamicQueryExecutor {

    private final UserTransactionRepository repository;

    public UserOfExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean checkOutNameQuery(String queryType) {
        return USER_OF.equalsIgnoreCase(queryType);
    }

    //Запрос проверяет, использует ли пользователь продукт X.
    @Override
    public boolean checkOutQuery(UUID userId, List<String> arguments, boolean negate) {
        String productType = arguments.get(0);
        boolean result = repository.countTransactionsByUserIdAndProductType(
                userId, productType) >= MIN_TRANSACTIONS_FOR_OF_USER;
        return negate != result;
    }
}

