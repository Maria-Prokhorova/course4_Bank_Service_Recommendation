package org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.parameter;

import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.enums.Operator;

import java.util.List;

public record TransactionCompareFourArgument(
        String productType,
        String transactionType,
        Operator operator,
        int value) {

    public static TransactionCompareFourArgument from(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new IllegalArgumentException("Expected 4 arguments");
        }
        String productType = arguments.get(0);
        String transactionType = arguments.get(1);
        Operator operator = Operator.from(arguments.get(2));
        int value = Integer.parseInt(arguments.get(3));

        return new TransactionCompareFourArgument(productType, transactionType, operator, value);
    }
}
