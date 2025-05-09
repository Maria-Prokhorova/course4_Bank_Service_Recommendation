package org.skypro.banking_service.ruleSystem.dynamicRuleSystem.args;

import org.skypro.banking_service.ruleSystem.dynamicRuleSystem.enums.ComparisonOperator;

import java.util.List;

public class TransactionCompareArgs {

    private final String productType;
    private final String transactionType;
    private final ComparisonOperator operator;
    private final int value;

    public TransactionCompareArgs(String productType, String transactionType, ComparisonOperator operator, int value) {
        this.productType = productType;
        this.transactionType = transactionType;
        this.operator = operator;
        this.value = value;
    }

    public static TransactionCompareArgs from(List<String> args) {
        if (args.size() != 4) {
            throw new IllegalArgumentException("Expected 4 arguments: productType, transactionType, operator, value");
        }

        String productType = args.get(0);
        String transactionType = args.get(1);
        ComparisonOperator operator = ComparisonOperator.from(args.get(2));
        int value = Integer.parseInt(args.get(3));

        return new TransactionCompareArgs(productType, transactionType, operator, value);
    }

    public String productType() {
        return productType;
    }

    public String transactionType() {
        return transactionType;
    }

    public ComparisonOperator operator() {
        return operator;
    }

    public int value() {
        return value;
    }
}
