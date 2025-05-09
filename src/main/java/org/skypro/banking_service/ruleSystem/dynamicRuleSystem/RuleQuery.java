package org.skypro.banking_service.ruleSystem.dynamicRuleSystem;

import org.skypro.banking_service.model.RuleCondition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RuleQuery {
    private final List<ConditionExecutor> executors;

    public RuleQuery(List<ConditionExecutor> executors) {
        this.executors = executors;
    }

    public boolean ruleQuery(RuleCondition condition, UUID userId) {
        if (condition == null || userId == null) {
            throw new IllegalArgumentException("Condition and userId cannot be null");
        }

        // Находим подходящий обработчик правил
        ConditionExecutor executor = executors.stream()
                .filter(e -> e.supports(condition.getQuery()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No executor for query: " + condition.getQuery()));

        return executor.evaluate(userId, condition.getArguments(), condition.isNegate());
    }
}


