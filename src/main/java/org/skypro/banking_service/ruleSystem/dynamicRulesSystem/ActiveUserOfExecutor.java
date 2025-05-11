package org.skypro.banking_service.ruleSystem.dynamicRulesSystem;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.skypro.banking_service.constants.DynamicRuleOfConstants.*;

@Component
public class  ActiveUserOfExecutor implements ConditionExecutor {

    private final UserTransactionRepository repository;

    public ActiveUserOfExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(String queryType) {
        return ACTIVE_USER_OF.equalsIgnoreCase(queryType);
    }

    @Override
    public boolean evaluate(UUID userId, List<String> arguments, boolean negate) {
        String productType = arguments.get(0);
        boolean result = repository.countTransactionsByUserIdAndProductType(
                userId, productType) >= MIN_TRANSACTIONS_FOR_ACTIVE_USER;
        return negate != result;
    }
}

