package org.skypro.banking_service.rulesystem.dynasmicRuleSystem.condition;

import org.skypro.banking_service.model.DynamicRule;

import java.util.UUID;

public interface DynamicRuleEvaluator {

    boolean evaluate(DynamicRule rule, UUID userId);
}

