package org.skypro.banking_service.rule_system.dinamic_rules.queries;

import org.skypro.banking_service.rule_system.dinamic_rules.parameter.QueryParameters;

public interface DimanicQueryExecutor {

    boolean checkOutQuery(QueryParameters params);

    boolean checkOutNameQuery (String nameQuery);
}
