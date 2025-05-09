package org.skypro.banking_service.ruleSystem.dynamicRuleSystem;

import org.skypro.banking_service.repository.UserTransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.skypro.banking_service.constants.DynamicRuleOfConstants.MIN_TRANSACTIONS_FOR_ACTIVE_USER;
import static org.skypro.banking_service.constants.DynamicRuleOfConstants.QUERY_TYPE;

@Component
public class ActiveUserOfExecutor implements ConditionExecutor {

    private final UserTransactionRepository repository;

    public ActiveUserOfExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(String queryType) {
        return QUERY_TYPE.equalsIgnoreCase(queryType);
    }

    @Override
    public boolean evaluate(UUID userId, List<String> args, boolean negate) {
        String productType = args.get(0);
        boolean result = repository.countTransactionsByUserIdAndProductType(
                userId, productType) >= MIN_TRANSACTIONS_FOR_ACTIVE_USER;
        return negate != result;
    }
}

