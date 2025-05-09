package org.skypro.banking_service.rulesystem.dynasmicRuleSystem;

import org.skypro.banking_service.repository.UserTransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class TransactionSumCompareExecutor implements ConditionExecutor {

    private final UserTransactionRepository repository;

    public TransactionSumCompareExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(String queryType) {
        return "TRANSACTION_SUM_COMPARE".equalsIgnoreCase(queryType);
    }

    @Override
    public boolean evaluate(UUID userId, List<String> args, boolean negate) {
        String productType = args.get(0);
        String transactionType = args.get(1);
        String operator = args.get(2);
        int value = Integer.parseInt(args.get(3));

        long sum = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                userId,
                productType,
                transactionType);

        boolean result = switch (operator) {
            case ">" -> sum > value;
            case "<" -> sum < value;
            case "=" -> sum == value;
            case ">=" -> sum >= value;
            case "<=" -> sum <= value;
            default -> false;
        };

        return negate ? !result : result;
    }
}

