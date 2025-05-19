package org.skypro.banking_service.service.ruleSystem.statickRulesSystem;

import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.parameter.StaticRuleParameters;
import org.springframework.stereotype.Service;

import static org.skypro.banking_service.constants.TransactionTypeConstants.DEPOSIT;
import static org.skypro.banking_service.constants.TransactionTypeConstants.WITHDRAW;

@Service
public class StaticQueryExecutorImpl implements StaticQueryExecutor {

    private final UserTransactionRepository repository;

    public StaticQueryExecutorImpl(UserTransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Метод выполняет проверку на использование клиентом заданного банковского продукта.
     *
     * @param params userId - идентификатор клиента, typeProduct1 - тип используемого банковского продукта.
     * @return булевое значение: true - если проверка выполняется, в противном случает - false.
     */
    @Override
    public boolean isUsingProduct(StaticRuleParameters params) {
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
    public boolean isAmountDepositMoreLimit(StaticRuleParameters params) {
        return repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                params.userId(), params.typeProduct1(), DEPOSIT) > params.limit();
    }

    /**
     * Метод выполняет следующую проверку: сумма пополнений по первому ИЛИ по второму указанным продуктам больше или равна установленного лимита.
     *
     * @param params userId - идентификатор клиента, typeProduct1 - тип первого используемого банковского продукта,
     *               typeProduct2 - тип второго используемого банковского продукта, limit - значение установленного лимита.
     * @return булевое значение: true - если проверка выполняется, в противном случает - false.
     */
    @Override
    public boolean isAmountSeveralDepositsMoreOrEqualsLimit(StaticRuleParameters params) {
        long debitDeposits = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                params.userId(), params.typeProduct1(), DEPOSIT);
        long savingDeposits = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                params.userId(), params.typeProduct2(), DEPOSIT);
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
    public boolean isAmountWithdrawMoreLimit(StaticRuleParameters params) {
        return repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                params.userId(), params.typeProduct1(), WITHDRAW) > params.limit();
    }

    /**
     * Метод выполняет проверку, чтобы сумма пополнений по всем продуктам заданного типа была больше,
     * чем сумма трат по всем продуктам заданного типа.
     *
     * @param params userId - идентификатор клиента, typeProduct1 - тип используемого банковского продукта.
     * @return булевое значение: true - если проверка выполняется, в противном случает - false.
     */
    @Override
    public boolean isAmountDepositsMoreThanWithdrawals(StaticRuleParameters params) {
        long debitDeposits = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                params.userId(), params.typeProduct1(), DEPOSIT);
        long debitWithdrawals = repository.findTotalAmountByUserIdAndProductTypeAndTransactionType(
                params.userId(), params.typeProduct1(), WITHDRAW);
        return debitDeposits > debitWithdrawals;
    }
}

