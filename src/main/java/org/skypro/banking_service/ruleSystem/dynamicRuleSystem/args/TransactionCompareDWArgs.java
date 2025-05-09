package org.skypro.banking_service.ruleSystem.dynamicRuleSystem.args;

import org.skypro.banking_service.ruleSystem.dynamicRuleSystem.enums.ComparisonOperator;

import java.util.List;

public class TransactionCompareDWArgs {

    private final String productType;
    private final ComparisonOperator operator;

    public TransactionCompareDWArgs(String productType, ComparisonOperator operator) {
        this.productType = productType;
        this.operator = operator;
    }

    public static TransactionCompareDWArgs from(List<String> args) {
        if (args.size() != 2) {
            throw new IllegalArgumentException("Expected 2 arguments: productType, operator");
        }

        String productType = args.get(0);
        ComparisonOperator operator = ComparisonOperator.from(args.get(1));

        return new TransactionCompareDWArgs(productType, operator);
    }

    public String productType() {
        return productType;
    }

    public ComparisonOperator operator() {
        return operator;
    }
}
