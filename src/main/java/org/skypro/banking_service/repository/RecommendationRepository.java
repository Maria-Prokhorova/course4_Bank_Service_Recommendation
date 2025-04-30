package org.skypro.banking_service.repository;

import java.util.List;
import java.util.UUID;

public interface RecommendationRepository {

    List<String> findUsedProductTypesByUserId(UUID userId);

    long findTotalDepositByUserIdAndProductType(UUID userId, String productType);

    long findTotalWithdrawByUserIdAndProductType(UUID userId, String productType);

    boolean existsUserProductByType(UUID userId, String productType);

    boolean userExists(UUID userId);
}
