package org.skypro.banking_service.repositories.h2.repository;

import java.util.List;
import java.util.UUID;

public interface UserTransactionRepository {

    List<String> findUsedProductTypesByUserId(UUID userId);

    List<String> findUsedProductByUserIdAndProductType(UUID userId, String productType);

    long findTotalDepositByUserIdAndProductType(UUID userId, String productType);

    long findTotalWithdrawByUserIdAndProductType(UUID userId, String productType);

    long findSumTransactionByUserIdAndProductType(UUID userId, String productType, String transactionType);

    boolean existsUserProductByType(UUID userId, String productType);

    boolean userExists(UUID userId);
}
