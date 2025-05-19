package org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries;

import java.util.List;
import java.util.UUID;

public interface DimanicQueryExecutor {

    // Метод проверяет тип запроса, соответствует ли он поддерживаемому системой (система обрабатывает всего 4 типа запросов).
    boolean checkOutNameQuery(String queryType);

    // Метод проверяет выполнение клиентом требований заданного запроса.
    boolean checkOutQuery(UUID userId, List<String> arguments, boolean negate);
}
