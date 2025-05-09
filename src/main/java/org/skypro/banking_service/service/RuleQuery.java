package org.skypro.banking_service.service;

import org.skypro.banking_service.model.RuleCondition;
import org.skypro.banking_service.repository.UserTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RuleQuery {

    private final UserTransactionRepository transactionRepository;

    public RuleQuery(UserTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public boolean ruleQuery(RuleCondition condition, UUID userId) {
        String query = condition.getQuery();
        List<String> args = condition.getArguments();
        boolean negate = condition.isNegate();

        boolean result;

        switch (query) {
            case "USER_OF":
                result = transactionRepository.existsUserProductByType(userId, args.get(0));
                break;
            case "ACTIVE_USER_OF":
                result = transactionRepository.countTransactionsByUserIdAndProductType(userId, args.get(0)) >= 5;
                break;
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW":
                long deposit = transactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                        userId, args.get(0),"DEPOSIT");
                long withdraw = transactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                        userId, args.get(0), "WITHDRAW");
                result = deposit > withdraw;
                break;
            case "TRANSACTION_SUM_COMPARE":
                String productType = args.get(0);
                String transactionType = args.get(1);
                String operator = args.get(2);
                long expected = Long.parseLong(args.get(3));

                long actual = transactionRepository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                        userId, productType, transactionType);

                result = switch (operator) {
                    case ">" -> actual > expected;
                    case "<" -> actual < expected;
                    case "=" -> actual == expected;
                    case ">=" -> actual >= expected;
                    case "<=" -> actual <= expected;
                    default -> throw new IllegalArgumentException("Unknown operator: " + operator);
                };
                break;
            default:
                throw new IllegalArgumentException("Unknown query: " + query);
        }

        return negate ? !result : result;
    }
}


