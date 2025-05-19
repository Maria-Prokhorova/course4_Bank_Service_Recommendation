package org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.enums;

public enum Operator {
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

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public abstract boolean compare(long a, long b);

    public static Operator from(String symbol) {
        for (Operator op : values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unsupported operator: " + symbol);
    }
}

