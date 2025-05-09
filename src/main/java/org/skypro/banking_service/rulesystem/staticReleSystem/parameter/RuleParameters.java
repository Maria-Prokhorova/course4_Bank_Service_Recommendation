package org.skypro.banking_service.rulesystem.staticReleSystem.parameter;

import java.util.UUID;

public record RuleParameters(
        UUID userId,
        String typeProduct1,
        String typeProduct2,
        long limit) {

    public static RuleParameters of(UUID userId, String typeProduct1) {
        return new RuleParameters(userId, typeProduct1, null, 0);
    }

    public static RuleParameters of(UUID userId, String typeProduct1, long limit) {
        return new RuleParameters(userId, typeProduct1, null, limit);
    }

    public static RuleParameters of(UUID userId, String typeProduct1, String typeProduct2, long limit) {
        return new RuleParameters(userId, typeProduct1, typeProduct2, limit);
    }
}
