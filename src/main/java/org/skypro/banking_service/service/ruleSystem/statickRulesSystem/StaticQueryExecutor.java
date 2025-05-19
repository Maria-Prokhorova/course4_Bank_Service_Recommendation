package org.skypro.banking_service.service.ruleSystem.statickRulesSystem;

import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.parameter.StaticRuleParameters;

public interface StaticQueryExecutor {

    // Проверка на использование пользователем заданного продукта.
    boolean isUsingProduct(StaticRuleParameters params);

    // Сумма пополнений продуктов с заданным типом больше установленного лимита.
    boolean isAmountDepositMoreLimit(StaticRuleParameters params);

    /*Сумма пополнений по первому указанному продукту больше или равна указанного лимита.
        ИЛИ Сумма пополнений по второму указанному продуктам больше или равна указанного лимита.*/
    boolean isAmountSeveralDepositsMoreOrEqualsLimit(StaticRuleParameters params);

    // Сумма трат по заданному продукту больше установленного лимита.
    boolean isAmountWithdrawMoreLimit(StaticRuleParameters params);

    /*Сумма пополнений по всем продуктам заданного типа больше, чем сумма трат по всем
    продуктам заданного типа.*/
    boolean isAmountDepositsMoreThanWithdrawals(StaticRuleParameters params);
}
