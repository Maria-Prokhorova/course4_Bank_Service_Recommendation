package org.skypro.banking_service.ruleSystem.dynamicRuleSystem;

import org.skypro.banking_service.repository.UserTransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.skypro.banking_service.constants.DynamicRuleOfConstants.USER_OF;

@Component
public class UserOfExecutor implements ConditionExecutor {
    private final UserTransactionRepository repository;

    public UserOfExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(String queryType) {
        return USER_OF.equals(queryType);
    }

    @Override
    public boolean evaluate(UUID userId, List<String> args, boolean negate) {
        if (userId == null || args == null || args.isEmpty()) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        boolean result = repository.existsUserProductByType(userId, args.get(0));
        return negate != result;
    }
}

