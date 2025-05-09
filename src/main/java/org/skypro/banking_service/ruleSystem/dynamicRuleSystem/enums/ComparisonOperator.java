package org.skypro.banking_service.ruleSystem.dynamicRuleSystem.enums;

public enum ComparisonOperator {
    GT(">") {
        public boolean compare(long a, long b) {
            return a > b;
        }
    },
    LT("<") {
        public boolean compare(long a, long b) {
            return a < b;
        }
    },
    EQ("=") {
        public boolean compare(long a, long b) {
            return a == b;
        }
    },
    GTE(">=") {
        public boolean compare(long a, long b) {
            return a >= b;
        }
    },
    LTE("<=") {
        public boolean compare(long a, long b) {
            return a <= b;
        }
    };

    private final String symbol;

    ComparisonOperator(String symbol) {
        this.symbol = symbol;
    }

    public abstract boolean compare(long a, long b);

    public static ComparisonOperator from(String symbol) {
        for (ComparisonOperator op : values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unsupported operator: " + symbol);
    }
}

