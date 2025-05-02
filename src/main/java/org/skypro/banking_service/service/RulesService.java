package org.skypro.banking_service.service;

import org.skypro.banking_service.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RulesService {

    private final RecommendationRepository repository;

    public RulesService(RecommendationRepository repository) {
        this.repository = repository;
    }

    // Проверка на использование пользователем заданного продукта
    public boolean isUsingProduct(UUID userId, String typeProduct) {
        return repository.existsUserProductByType(userId, typeProduct);
    }

    // Сумма пополнений продуктов с заданным типом больше установленного лимита.
    public boolean isAmountDepositMoreLimit(UUID userId, String typeProduct, long sumLimit) {
        return repository.findTotalDepositByUserIdAndProductType(userId, typeProduct) > sumLimit;
    }

    // Сумма пополнений по первому указанному продукту больше или равна указанного лимита.
    // ИЛИ Сумма пополнений по второму указанному продуктам больше или равна указанного лимита.
    public boolean isAmountSeveralDepositsMoreOrEqualsLimit(UUID userId, String typeProduct1, String typeProduct2, long sumLimit) {
        long debitDeposits = repository.findTotalDepositByUserIdAndProductType(userId, typeProduct1);
        long savingDeposits = repository.findTotalDepositByUserIdAndProductType(userId, typeProduct2);
        return debitDeposits >= sumLimit || savingDeposits >= sumLimit;
    }

    // Сумма трат по заданному продукту больше установленного лимита.
    public boolean isAmountWithdrawMoreLimit(UUID userId, String typeProduct, long sumLimit) {
        return repository.findTotalWithdrawByUserIdAndProductType(userId, typeProduct) > sumLimit;
    }

    // Сумма пополнений по всем продуктам заданного типа больше, чем сумма трат по всем продуктам заданного типа.
    public boolean isAmountDepositsMoreThanWithdrawals(UUID userId, String typeProduct) {
        long debitDeposits = repository.findTotalDepositByUserIdAndProductType(userId, typeProduct);
        long debitWithdrawals = repository.findTotalWithdrawByUserIdAndProductType(userId, typeProduct);
        return debitDeposits > debitWithdrawals;
    }
}
