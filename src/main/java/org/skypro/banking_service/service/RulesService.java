package org.skypro.banking_service.service;

import org.skypro.banking_service.ruleSystem.staticRuleSystem.parameter.RuleParameters;

public interface RulesService {

    boolean isUsingProduct(RuleParameters params);

    boolean isAmountDepositMoreLimit(RuleParameters params);

    boolean isAmountSeveralDepositsMoreOrEqualsLimit(RuleParameters params);

    boolean isAmountWithdrawMoreLimit(RuleParameters params);

    boolean isAmountDepositsMoreThanWithdrawals(RuleParameters params);
}
