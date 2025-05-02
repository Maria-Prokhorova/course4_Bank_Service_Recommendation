package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.repository.RecommendationRepository;
import org.skypro.banking_service.rulesystem.parameter.RuleParameters;
import org.skypro.banking_service.service.RulesService;
import org.springframework.stereotype.Service;

@Service
public class RulesServiceImpl implements RulesService {

    private final RecommendationRepository repository;

    public RulesServiceImpl(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isUsingProduct(RuleParameters params) {
        return repository.existsUserProductByType(
                params.userId(), params.typeProduct1());
    }

    @Override
    public boolean isAmountDepositMoreLimit(RuleParameters params) {
        return repository.findTotalDepositByUserIdAndProductType(
                params.userId(), params.typeProduct1()) > params.limit();
    }

    @Override
    public boolean isAmountSeveralDepositsMoreOrEqualsLimit(RuleParameters params) {
        long debitDeposits = repository.findTotalDepositByUserIdAndProductType(
                params.userId(), params.typeProduct1());
        long savingDeposits = repository.findTotalDepositByUserIdAndProductType(
                params.userId(), params.typeProduct2());
        return debitDeposits >= params.limit() || savingDeposits >= params.limit();
    }

    @Override
    public boolean isAmountWithdrawMoreLimit(RuleParameters params) {
        return repository.findTotalWithdrawByUserIdAndProductType(
                params.userId(), params.typeProduct1()) > params.limit();
    }

    @Override
    public boolean isAmountDepositsMoreThanWithdrawals(RuleParameters params) {
        long debitDeposits = repository.findTotalDepositByUserIdAndProductType(
                params.userId(), params.typeProduct1());
        long debitWithdrawals = repository.findTotalWithdrawByUserIdAndProductType(
                params.userId(), params.typeProduct1());
        return debitDeposits > debitWithdrawals;
    }
}

