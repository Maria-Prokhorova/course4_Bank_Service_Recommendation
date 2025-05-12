package org.skypro.banking_service.constants;

public class DinamicRuleConstant {
//    public static final String ACTIVE_USER_OF = "ACTIVE_USER_OF";
//    public static final String USER_OF = "USER_OF";
//    public static final String TRANSACTION_SUM_COMPARE = "TRANSACTION_SUM_COMPARE";
//    public static final String TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW = "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW";
//
    private DinamicRuleConstant() {
    }

    public enum TypeQuery {
        ACTIVE_USER_OF,
        USER_OF,
        TRANSACTION_SUM_COMPARE,
        TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW;
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