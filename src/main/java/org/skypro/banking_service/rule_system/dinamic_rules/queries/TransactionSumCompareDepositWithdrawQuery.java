package org.skypro.banking_service.rule_system.dinamic_rules.queries;

import org.skypro.banking_service.constants.DinamicRuleConstant;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.rule_system.dinamic_rules.ComparisonOperator;
import org.skypro.banking_service.rule_system.dinamic_rules.parameter.QueryParameters;
import org.springframework.stereotype.Component;

@Component
public class TransactionSumCompareDepositWithdrawQuery implements DimanicQueryExecutor {

    private final UserTransactionRepository repository;

    public TransactionSumCompareDepositWithdrawQuery(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean checkOutNameQuery(String queryType) {
        return DinamicRuleConstant.TypeQuery.TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW.equals(queryType);
    }

    //Запрос сравнивает сумму всех транзакций типа DEPOSIT с суммой всех транзакций типа
    //WITHDRAW по продукту X.
    @Override
    public boolean checkOutQuery(QueryParameters params) {
        long deposit = repository.findTotalDepositByUserIdAndProductType(params.userId(), params.typeProduct());
        long withdraw = repository.findTotalWithdrawByUserIdAndProductType(params.userId(), params.typeProduct());
        boolean result = ComparisonOperator.compare(deposit, withdraw, params.typeOperator());
        return params.negate() != result;
    }
}
