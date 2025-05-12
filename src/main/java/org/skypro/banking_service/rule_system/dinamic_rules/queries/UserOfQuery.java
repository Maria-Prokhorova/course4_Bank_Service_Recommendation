package org.skypro.banking_service.rule_system.dinamic_rules.queries;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.rule_system.dinamic_rules.parameter.QueryParameters;
import org.springframework.stereotype.Component;

import static org.skypro.banking_service.constants.DinamicRuleConstant.TypeQuery.USER_OF;

@Component
public class UserOfQuery implements DimanicQueryExecutor {

    private final UserTransactionRepository repository;

    public UserOfQuery(UserTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean checkOutNameQuery(String queryType) {
        return USER_OF.getName().equalsIgnoreCase(queryType);
    }

    //Запрос проверяет, использует ли пользователь продукт X.
    @Override
    public boolean checkOutQuery(QueryParameters params) {
        boolean result = repository.existsUserProductByType(
                params.userId(), params.typeProduct());
        return params.negate() != result;
    }
}
