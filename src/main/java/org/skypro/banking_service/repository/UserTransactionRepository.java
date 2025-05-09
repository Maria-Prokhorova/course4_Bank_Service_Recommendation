package org.skypro.banking_service.repository;

import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.UUID;

public interface UserTransactionRepository {

    List<String> findUsedProductTypesByUserId(UUID userId);

    long findTotalAmountByUserIdAndProductTypeAndTransactionType(UUID userId, String productType,
                                                                 String transactionType);
    boolean existsUserProductByType(UUID userId, String productType);

    boolean userExists(UUID userId);

    int countTransactionsByUserIdAndProductType(UUID userId, String productType);
}
