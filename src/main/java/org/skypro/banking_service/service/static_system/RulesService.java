package org.skypro.banking_service.service.static_system;

import org.skypro.banking_service.rule_system.dinamic_rules.parameter.RuleParameters;

public interface RulesService {
    // Проверка на использование пользователем заданного продукта
    boolean isUsingProduct(RuleParameters params);

    // Сумма пополнений продуктов с заданным типом больше установленного лимита.
    boolean isAmountDepositMoreLimit(RuleParameters params);

    /*Сумма пополнений по первому указанному продукту больше или равна указанного лимита.
        ИЛИ Сумма пополнений по второму указанному продуктам больше или равна указанного лимита.*/
    boolean isAmountSeveralDepositsMoreOrEqualsLimit(RuleParameters params);

    // Сумма трат по заданному продукту больше установленного лимита.
    boolean isAmountWithdrawMoreLimit(RuleParameters params);

    /*Сумма пополнений по всем продуктам заданного типа больше, чем сумма трат по всем
    продуктам заданного типа.*/
    boolean isAmountDepositsMoreThanWithdrawals(RuleParameters params);
}
