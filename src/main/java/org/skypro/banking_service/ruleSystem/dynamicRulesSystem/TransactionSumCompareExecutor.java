package org.skypro.banking_service.ruleSystem.dynamicRulesSystem;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.argumets.TransactionCompareFourArgument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.skypro.banking_service.constants.DynamicRuleOfConstants.TRANSACTION_SUM_COMPARE;

@Component
public class TransactionSumCompareExecutor implements ConditionExecutor {

    private final UserTransactionRepository repository;

    public TransactionSumCompareExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(String queryType) {
        return TRANSACTION_SUM_COMPARE.equalsIgnoreCase(queryType);
    }

    @Override
    public boolean evaluate(UUID userId, List<String> arguments, boolean negate) {
        TransactionCompareFourArgument parsed = TransactionCompareFourArgument.from(arguments);

        long totalAmount = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                userId,
                parsed.productType(),
                parsed.transactionType());

        boolean result = parsed.operator().compare(totalAmount, parsed.value());
        return negate != result;
    }
}
