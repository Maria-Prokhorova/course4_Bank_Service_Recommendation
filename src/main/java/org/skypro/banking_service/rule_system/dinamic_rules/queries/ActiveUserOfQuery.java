package org.skypro.banking_service.rule_system.dinamic_rules.queries;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.rule_system.dinamic_rules.parameter.QueryParameters;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.skypro.banking_service.constants.DinamicRuleConstant.MIN_LIMIT_TRANSACTION;
import static org.skypro.banking_service.constants.DinamicRuleConstant.TypeQuery.ACTIVE_USER_OF;

@Component
public class ActiveUserOfQuery implements DimanicQueryExecutor {

    private final UserTransactionRepository repository;

    public ActiveUserOfQuery(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean checkOutNameQuery(String queryType) {
        return ACTIVE_USER_OF.getName().equalsIgnoreCase(queryType);
    }

    //Запрос проверяет, является ли пользователь, активным пользователем продукта X (есть 5 транзакций).
    @Override
    public boolean checkOutQuery(QueryParameters params) {
        List<String> listProductUser = repository.findUsedProductByUserIdAndProductType(params.userId(), params.typeProduct());
        boolean result;
        if (listProductUser.size() >= MIN_LIMIT_TRANSACTION) {
            result = true;
        } else result = false;
        return params.negate() != result;
    }
}
