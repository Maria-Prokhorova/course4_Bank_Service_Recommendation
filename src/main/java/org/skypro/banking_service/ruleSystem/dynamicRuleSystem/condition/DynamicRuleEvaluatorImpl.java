package org.skypro.banking_service.ruleSystem.dynamicRuleSystem.condition;

import org.skypro.banking_service.model.DynamicRule;
import org.skypro.banking_service.model.RuleCondition;
import org.skypro.banking_service.ruleSystem.dynamicRuleSystem.RuleQuery;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DynamicRuleEvaluatorImpl implements DynamicRuleEvaluator {

    private final RuleQuery ruleQuery;

    public DynamicRuleEvaluatorImpl(RuleQuery ruleQuery) {
        this.ruleQuery = ruleQuery;
    }

    @Override
    public boolean evaluate(DynamicRule rule, UUID userId) {
        for (RuleCondition condition : rule.getConditions()) {
            boolean result = ruleQuery.ruleQuery(condition, userId);
            if (!result) {
                return false;
            }
        }
        return true;
    }
}

