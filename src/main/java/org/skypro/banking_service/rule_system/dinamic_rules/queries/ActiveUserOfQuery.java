package org.skypro.banking_service.rule_system.dinamic_rules.queries;

import org.skypro.banking_service.constants.DinamicRuleConstant;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.rule_system.dinamic_rules.parameter.QueryParameters;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActiveUserOfQuery implements DimanicQueryExecutor {

    private final UserTransactionRepository repository;

    public ActiveUserOfQuery(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean checkOutNameQuery(String queryType) {
        return DinamicRuleConstant.TypeQuery.ACTIVE_USER_OF.equals(queryType);
    }

    //Запрос проверяет, является ли пользователь, активным пользователем продукта X (есть 5 транзакций).
    @Override
    public boolean checkOutQuery(QueryParameters params) {
        List<String> listProductUser = repository.findUsedProductTypesByUserId(params.userId());
        boolean result;
        if (listProductUser.size() >= 5) {
            result = true;
        } else result = false;
        return params.negate() != result;
    }
}
