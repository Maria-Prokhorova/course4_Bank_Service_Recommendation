package org.skypro.banking_service.rule_system.dinamic_rules.parameter;

import java.util.UUID;

public record QueryParameters(
        UUID userId,
        String typeProduct,
        String typeTransaction,
        String typeOperator,
        int limit,
        boolean negate) {

    public static QueryParameters of(UUID userId, String typeProduct, boolean negate) {
        return new QueryParameters(userId, typeProduct, null, null, 0, negate);
    }

    public static QueryParameters of(UUID userId, String typeProduct, String typeOperator, boolean negate) {
        return new QueryParameters(userId, typeProduct, typeOperator, null, 0, negate);
    }

    public static QueryParameters of(UUID userId, String typeProduct, String typeTransaction, String typeOperator, int limit, boolean negate) {
        return new QueryParameters(userId, typeProduct, typeTransaction, typeOperator, limit, negate);
    }
}
