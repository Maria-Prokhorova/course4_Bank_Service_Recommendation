package org.skypro.banking_service.constants;

public class DynamicRuleOfConstants {
    public static final int MIN_TRANSACTIONS_FOR_ACTIVE_USER = 5;
    public static final int REQUIRED_ARGS_COUNT = 1;
    public static final String QUERY_TYPE = "ACTIVE_USER_OF";
    public static final String USER_OF = "USER_OF";
    public static final String TRANSACTION_SUM_COMPARE = "TRANSACTION_SUM_COMPARE";
    public static final String TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW = "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW";

    private DynamicRuleOfConstants() {
    }
}
