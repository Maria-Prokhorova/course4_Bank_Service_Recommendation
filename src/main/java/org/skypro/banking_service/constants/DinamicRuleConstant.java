package org.skypro.banking_service.constants;

public class DinamicRuleConstant {

    private DinamicRuleConstant() {
    }

    public static final int MIN_LIMIT_TRANSACTION = 5;

    public enum TypeQuery {
        ACTIVE_USER_OF("ACTIVE_USER_OF"),
        USER_OF("USER_OF"),
        TRANSACTION_SUM_COMPARE("TRANSACTION_SUM_COMPARE"),
        TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");

        private final String name;

        TypeQuery(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum TypeProduct {
        DEBIT,
        CREDIT,
        INVEST,
        SAVING;
    }

    public enum TypeTransaction {
        WITHDRAW,
        DEPOSIT;
    }
}