package org.skypro.banking_service.ruleSystem.dynamicRulesSystem;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.argumets.TransactionCompareTwoArguments;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.enums.Operator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static org.skypro.banking_service.constants.TransactionTypeConstants.DEPOSIT;
import static org.skypro.banking_service.constants.TransactionTypeConstants.WITHDRAW;
import static org.skypro.banking_service.constants.DynamicRuleOfConstants.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW;

@Component
public class TransactionSumCompareDepositWithdrawExecutor implements ConditionExecutor {

    private final UserTransactionRepository repository;

    public TransactionSumCompareDepositWithdrawExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(String queryType) {
        return TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW.equalsIgnoreCase(queryType);
    }

    @Override
    public boolean evaluate(UUID userId, List<String> arguments, boolean negate) {
        TransactionCompareTwoArguments parsed = TransactionCompareTwoArguments.from(arguments);

        long totalDeposit = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                userId, parsed.productType(), DEPOSIT);
        long totalWithdraw = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                userId, parsed.productType(), WITHDRAW);


        boolean result = parsed.operator().compare(totalDeposit, totalWithdraw);
        return negate != result;
    }
}
