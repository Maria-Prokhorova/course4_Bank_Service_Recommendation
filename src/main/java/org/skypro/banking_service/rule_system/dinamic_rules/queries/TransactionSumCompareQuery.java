package org.skypro.banking_service.rule_system.dinamic_rules.queries;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.rule_system.dinamic_rules.ComparisonOperator;
import org.skypro.banking_service.rule_system.dinamic_rules.parameter.QueryParameters;
import org.springframework.stereotype.Component;

import static org.skypro.banking_service.constants.DinamicRuleConstant.TypeQuery.TRANSACTION_SUM_COMPARE;

@Component
public class TransactionSumCompareQuery implements DimanicQueryExecutor {

    private final UserTransactionRepository repository;

    public TransactionSumCompareQuery(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean checkOutNameQuery(String queryType) {
        return TRANSACTION_SUM_COMPARE.equals(queryType);
    }

    //Запрос сравнивает сумму всех транзакций типа Y по продуктам типа X с некоторой константой C.
    @Override
    public boolean checkOutQuery(QueryParameters params) {
        long sumTransaction = repository.findSumTransactionByUserIdAndProductType(params.userId(), params.typeProduct(), params.typeTransaction());
        boolean result = ComparisonOperator.compare(sumTransaction, params.limit(), params.typeOperator());
        return params.negate() != result;
    }
}
