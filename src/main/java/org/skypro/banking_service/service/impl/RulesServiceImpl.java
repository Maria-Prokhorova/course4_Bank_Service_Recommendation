package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepositoryImpl;
import org.skypro.banking_service.rulesystem.parameter.RuleParameters;
import org.skypro.banking_service.service.RulesService;
import org.springframework.stereotype.Service;

@Service
public class RulesServiceImpl implements RulesService {

    private final UserTransactionRepositoryImpl repository;

    public RulesServiceImpl(UserTransactionRepositoryImpl repository) {
        this.repository = repository;
    }

    /**
     * Метод выполняет проверку на использование клиентом заданного банковского продукта.
     *
     * @param params userId - идентификатор клиента, typeProduct1 - тип используемого банковского продукта.
     * @return булевое значение: true - если проверка выполняется, в противном случает - false.
     */
    @Override
    public boolean isUsingProduct(RuleParameters params) {
        return repository.existsUserProductByType(
                params.userId(), params.typeProduct1());
    }

    /**
     * Метод выполняет следующую проверку: сумма пополнений продуктов с заданным типом больше установленного лимита.
     *
     * @param params userId - идентификатор клиента, typeProduct1 - тип используемого банковского продукта,
     *               limit - значение установленного лимита.
     * @return булевое значение: true - если проверка выполняется, в противном случает - false.
     */
    @Override
    public boolean isAmountDepositMoreLimit(RuleParameters params) {
        return repository.findTotalDepositByUserIdAndProductType(
                params.userId(), params.typeProduct1()) > params.limit();
    }

    /**
     * Метод выполняет следующую проверку: сумма пополнений по первому ИЛИ по второму указанным продуктам больше или равна установленного лимита.
     *
     * @param params userId - идентификатор клиента, typeProduct1 - тип первого используемого банковского продукта,
     *               typeProduct2 - тип второго используемого банковского продукта, limit - значение установленного лимита.
     * @return булевое значение: true - если проверка выполняется, в противном случает - false.
     */
    @Override
    public boolean isAmountSeveralDepositsMoreOrEqualsLimit(RuleParameters params) {
        long debitDeposits = repository.findTotalDepositByUserIdAndProductType(
                params.userId(), params.typeProduct1());
        long savingDeposits = repository.findTotalDepositByUserIdAndProductType(
                params.userId(), params.typeProduct2());
        return debitDeposits >= params.limit() || savingDeposits >= params.limit();
    }

    /**
     * Метод выполняет проверку, чтобы сумма трат по заданному продукту была больше установленного лимита.
     *
     * @param params userId - идентификатор клиента, typeProduct1 - тип используемого банковского продукта,
     *               limit - значение установленного лимита.
     * @return булевое значение: true - если проверка выполняется, в противном случает - false.
     */
    @Override
    public boolean isAmountWithdrawMoreLimit(RuleParameters params) {
        return repository.findTotalWithdrawByUserIdAndProductType(
                params.userId(), params.typeProduct1()) > params.limit();
    }

    /**
     * Метод выполняет проверку, чтобы сумма пополнений по всем продуктам заданного типа была больше,
     * чем сумма трат по всем продуктам заданного типа.
     *
     * @param params userId - идентификатор клиента, typeProduct1 - тип используемого банковского продукта.
     * @return булевое значение: true - если проверка выполняется, в противном случает - false.
     */
    @Override
    public boolean isAmountDepositsMoreThanWithdrawals(RuleParameters params) {
        long debitDeposits = repository.findTotalDepositByUserIdAndProductType(
                params.userId(), params.typeProduct1());
        long debitWithdrawals = repository.findTotalWithdrawByUserIdAndProductType(
                params.userId(), params.typeProduct1());
        return debitDeposits > debitWithdrawals;
    }
}

