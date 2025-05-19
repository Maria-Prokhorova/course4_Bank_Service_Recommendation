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
     *
     *
     * @param queries
     * @param userId
     * @return
     */
    @Override
    public boolean checkOutDinamicRule(QueryRules queries, UUID userId) {
        if (queries == null || userId == null) {
            throw new QueryEvaluationException("Condition and userId cannot be null");
        }

        // Находим подходящий обработчик запросов (система поддерживает 4 типа запросов)
        DimanicQueryExecutor executor = executors.stream()
                .filter(e -> e.checkOutNameQuery(queries.getQuery()))
                .findFirst()
                .orElseThrow(() -> new QueryEvaluationException("No executor for query: " + queries.getQuery()));

        return executor.checkOutQuery(userId, queries.getArguments(), queries.isNegate());
    }
}


