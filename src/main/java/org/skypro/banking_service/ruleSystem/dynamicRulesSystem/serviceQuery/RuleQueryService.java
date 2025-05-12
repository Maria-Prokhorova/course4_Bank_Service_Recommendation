package org.skypro.banking_service.ruleSystem.dynamicRulesSystem.serviceQuery;

import org.skypro.banking_service.exception.QueryEvaluationException;
import org.skypro.banking_service.model.Queries;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.ConditionExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RuleQueryService {

    private final List<ConditionExecutor> executors;

    public RuleQueryService(List<ConditionExecutor> executors) {
        this.executors = executors;
    }

    public boolean ruleQuery(Queries queries, UUID userId) {
        if (queries == null || userId == null) {
            throw new QueryEvaluationException("Condition and userId cannot be null");
        }

        // Находим подходящий обработчик правил
        ConditionExecutor executor = executors.stream()
                .filter(e -> e.supports(queries.getQuery()))
                .findFirst()
                .orElseThrow(() -> new QueryEvaluationException("No executor for query: " + queries.getQuery()));

        return executor.evaluate(userId, queries.getArguments(), queries.isNegate());
    }
}


