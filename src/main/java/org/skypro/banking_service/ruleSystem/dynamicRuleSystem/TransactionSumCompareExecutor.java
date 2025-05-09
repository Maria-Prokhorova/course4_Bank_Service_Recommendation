package org.skypro.banking_service.ruleSystem.dynamicRuleSystem;

import org.skypro.banking_service.repository.UserTransactionRepository;
import org.skypro.banking_service.ruleSystem.dynamicRuleSystem.args.TransactionCompareArgs;
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
    public boolean evaluate(UUID userId, List<String> args, boolean negate) {
        TransactionCompareArgs parsed = TransactionCompareArgs.from(args);

        long totalAmount = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                userId,
                parsed.productType(),
                parsed.transactionType()
        );

        boolean result = parsed.operator().compare(totalAmount, parsed.value());
        return negate ? !result : result;
    }
}
