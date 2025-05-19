package org.skypro.banking_service.service.ruleSystem.statickRulesSystem.parameter;

import java.util.UUID;

public record StaticRuleParameters(
        UUID userId,
        String typeProduct1,
        String typeProduct2,
        long limit) {

    public static StaticRuleParameters of(UUID userId, String typeProduct1) {
        return new StaticRuleParameters(userId, typeProduct1, null, 0);
    }

    public static StaticRuleParameters of(UUID userId, String typeProduct1, long limit) {
        return new StaticRuleParameters(userId, typeProduct1, null, limit);
    }

    public static StaticRuleParameters of(UUID userId, String typeProduct1, String typeProduct2, long limit) {
        return new StaticRuleParameters(userId, typeProduct1, typeProduct2, limit);
    }
}
