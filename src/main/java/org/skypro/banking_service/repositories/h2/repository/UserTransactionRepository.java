package org.skypro.banking_service.repositories.h2.repository;

import org.skypro.banking_service.telegramBot.dto.UserFullName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTransactionRepository {

    List<String> findUsedProductTypesByUserId(UUID userId);

    boolean existsUserProductByType(UUID userId, String productType);

    boolean userExists(UUID userId);

    int countTransactionsByUserIdAndProductType(UUID userId, String productType);

    long findTotalAmountByUserIdAndProductTypeAndTransactionType(
            UUID userId, String productType, String transactionType);

    Optional<UserFullName> findUserFullNameByUsername(String username);
}
