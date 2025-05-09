package org.skypro.banking_service.rulesystem.dynasmicRuleSystem;

import org.skypro.banking_service.repository.UserTransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserOfExecutor implements ConditionExecutor {

    private final UserTransactionRepository repository;

    public UserOfExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(String queryType) {
        return "USER_OF".equalsIgnoreCase(queryType);
    }

    @Override
    public boolean evaluate(UUID userId, List<String> args, boolean negate) {
        String productType = args.get(0);
        boolean result = repository.countTransactionsByUserIdAndProductType(userId, productType) > 0;
        return negate ? !result : result;
    }
}

