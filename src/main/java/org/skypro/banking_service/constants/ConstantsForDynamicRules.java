package org.skypro.banking_service.constants;

public class ConstantsForDynamicRules {

    public static final int MIN_TRANSACTIONS_FOR_ACTIVE_USER = 5;
    public static final int MIN_TRANSACTIONS_FOR_OF_USER = 1;

    // Типы запросов, поддерживаемых системой
    public static final String ACTIVE_USER_OF = "ACTIVE_USER_OF";
    public static final String USER_OF = "USER_OF";
    public static final String TRANSACTION_SUM_COMPARE = "TRANSACTION_SUM_COMPARE";
    public static final String TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW = "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW";

    private ConstantsForDynamicRules() {
    }

    // Тип продукта
    public enum TypeProduct {
        DEBIT,
        CREDIT,
        INVEST,
        SAVING;
    }

    // Тип транзакции
    public enum TypeTransaction {
        WITHDRAW,
        DEPOSIT;
    }
}