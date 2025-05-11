package org.skypro.banking_service.rule_system.dinamic_rules;

public class ComparisonOperator {

    private ComparisonOperator() {
    }

    public static boolean compare(long a, long b, String symbol) {
        if (symbol.equals(">")) {
            return a > b;
        }
        if (symbol.equals("<")) {
            return a < b;
        }
        if (symbol.equals(">=")) {
            return a >= b;
        }
        if (symbol.equals("<=")) {
            return a <= b;
        }
        if (symbol.equals("=")) {
            return a == b;
        }
        else throw new IllegalArgumentException("Unsupported operator: " + symbol);
    }
}
