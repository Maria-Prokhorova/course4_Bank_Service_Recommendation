package org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem;

import org.skypro.banking_service.exception.QueryEvaluationException;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.DimanicQueryExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DimanicRuleImp implements DimanicRule {

    private final List<DimanicQueryExecutor> executors;

    public DimanicRuleImp(List<DimanicQueryExecutor> executors) {
        this.executors = executors;
    }

    /**
     * Метод проверяет все запросы, которые есть в динамических правилах у заданного клиента.
     * При этом сначала по типу запроса подбирается нужный обработчик запроса, а затем выполняется проверка соблюдения условий запроса.
     *
     * @param queryRules - запрос.
     * @param userId - id клиента.
     * @return булевое значение: если условие по запросу выполняются - true, иначе - false.
     */
    @Override
    public boolean checkOutDinamicRule(QueryRules queryRules, UUID userId) {
        if (queryRules == null || userId == null) {
            throw new QueryEvaluationException("Condition and userId cannot be null");
        }

        // Находим подходящий обработчик запросов (система поддерживает 4 типа запросов),
        // в случае если ни один из обработчиков не подошел выбрасываем исключение
        DimanicQueryExecutor executor = executors.stream()
                .filter(e -> e.checkOutNameQuery(queryRules.getQuery()))
                .findFirst()
                .orElseThrow(() -> new QueryEvaluationException("No executor for query: " + queryRules.getQuery()));

        // для найденного обработчика вызываем метод по проверки выполнения запроса
        return executor.checkOutQuery(userId, queryRules.getArguments(), queryRules.isNegate());
    }
}


