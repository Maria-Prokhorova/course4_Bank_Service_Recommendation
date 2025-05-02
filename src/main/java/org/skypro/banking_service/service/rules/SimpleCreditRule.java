package org.skypro.banking_service.service.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.repository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.skypro.banking_service.service.constants.ProductConstants.*;

@Component
public class SimpleCreditRule implements RecommendationRule {

    private final RecommendationRepository repository;

    public SimpleCreditRule(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> checkOut(UUID userId) {
        if (isEligibleForSimpleCreditRule(userId)) {
            return buildRecommendationDto();
        }
        return Optional.empty();
    }

    private boolean isEligibleForSimpleCreditRule(UUID userId) {
        return !usesCreditProduct(userId)
                && hasLargeEnoughDeposits(userId)
                && depositsExceedWithdrawals(userId);
    }

    private boolean usesCreditProduct(UUID userId) {
        Set<String> products = new HashSet<>(repository.findUsedProductTypesByUserId(userId));
        return products.contains(TYPE_CREDIT);
    }

    private boolean depositsExceedWithdrawals(UUID userId) {
        long deposit = repository.findTotalDepositByUserIdAndProductType(userId, "DEBIT");
        long withdraw = repository.findTotalWithdrawByUserIdAndProductType(userId, "DEBIT");
        return deposit > withdraw;
    }

    private boolean hasLargeEnoughDeposits(UUID userId) {
        long withdraw = repository.findTotalWithdrawByUserIdAndProductType(userId, "DEBIT");
        return withdraw > LIMIT_SIMPLE_CREDIT;
    }

    private Optional<RecommendationDto> buildRecommendationDto() {
        return Optional.of(new RecommendationDto(
                PRODUCT_ID_SIMPLE_CREDIT,
                PRODUCT_NAME_SIMPLE_CREDIT,
                DESCRIPTION_SIMPLE_CREDIT)
        );
    }

}

