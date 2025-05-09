package org.skypro.banking_service.rulesystem.dynasmicRuleSystem;

import org.skypro.banking_service.repository.UserTransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ActiveUserOfExecutor implements ConditionExecutor {

    private final UserTransactionRepository repository;

    public ActiveUserOfExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(String queryType) {
        return "ACTIVE_USER_OF".equalsIgnoreCase(queryType);
    }

    @Override
    public boolean evaluate(UUID userId, List<String> args, boolean negate) {
        String productType = args.get(0);
        boolean result = repository.countTransactionsByUserIdAndProductType(userId, productType) >= 5;
        return negate ? !result : result;
    }
}

