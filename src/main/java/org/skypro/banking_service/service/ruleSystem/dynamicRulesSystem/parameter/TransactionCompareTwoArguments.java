package org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.parameter;

import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.enums.Operator;

import java.util.List;

public record TransactionCompareTwoArguments(
        String productType,
        Operator operator) {

    public static TransactionCompareTwoArguments from(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Expected 2 arguments");
        }

        String productType = arguments.get(0);
        Operator operator = Operator.from(arguments.get(1));

        return new TransactionCompareTwoArguments(productType, operator);
    }
}
