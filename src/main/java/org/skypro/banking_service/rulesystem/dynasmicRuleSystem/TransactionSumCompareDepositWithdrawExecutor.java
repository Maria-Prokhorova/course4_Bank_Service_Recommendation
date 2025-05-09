package org.skypro.banking_service.rulesystem.dynasmicRuleSystem;

import org.skypro.banking_service.repository.UserTransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class TransactionSumCompareDepositWithdrawExecutor implements ConditionExecutor {

    private final UserTransactionRepository repository;

    public TransactionSumCompareDepositWithdrawExecutor(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(String queryType) {
        return "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW".equalsIgnoreCase(queryType);
    }

    @Override
    public boolean evaluate(UUID userId, List<String> args, boolean negate) {
        String productType = args.get(0);
        String operator = args.get(1);

        long totalDeposit = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                userId, 
                productType, 
                "DEPOSIT");
        long totalWithdraw = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                userId, 
                productType, 
                "WITHDRAW");

        boolean result = switch (operator) {
            case ">" -> totalDeposit > totalWithdraw;
            case "<" -> totalDeposit < totalWithdraw;
            case "=" -> totalDeposit == totalWithdraw;
            case ">=" -> totalDeposit >= totalWithdraw;
            case "<=" -> totalDeposit <= totalWithdraw;
            default -> false;
        };

        return negate ? !result : result;
    }
}

