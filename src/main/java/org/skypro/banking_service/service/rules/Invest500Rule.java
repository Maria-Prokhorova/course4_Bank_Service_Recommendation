package org.skypro.banking_service.service.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.repository.RecommendationRepository;
import org.springframework.stereotype.Component;


import java.util.Optional;
import java.util.UUID;
import static org.skypro.banking_service.service.constants.ProductConstants.*;

@Component
public class Invest500Rule implements RecommendationRule {

    private final RecommendationRepository recommendationRepository;

    public Invest500Rule(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Optional<RecommendationDto> checkOut(UUID userId) {
        if (isEligibleForInvest500(userId)) {
            return Optional.of(buildingRecommendationDto());
        }
        return Optional.empty();
    }

    private boolean isEligibleForInvest500(UUID userId) {
        /**
         * checking a set of rules for a product - Invest 500
         */
        return isUsingDebitProduct(userId)
                && isNotUsingInvestProduct(userId)
                && isAmountDepositMoreThanOneHundred(userId);
    }

    private boolean isUsingDebitProduct(UUID userId) {
        return recommendationRepository.existsUserProductByType(userId, TYPE_DEBIT);
    }

    private boolean isNotUsingInvestProduct(UUID userId) {
        return !(recommendationRepository.existsUserProductByType(userId, TYPE_INVEST));
    }

    private boolean isAmountDepositMoreThanOneHundred(UUID userId) {
        return recommendationRepository.findTotalDepositByUserIdAndProductType(userId, TYPE_SAVING) > LIMIT_INVEST_500;
    }

    private RecommendationDto buildingRecommendationDto() {
        return new RecommendationDto(
                PRODUCT_ID_INVEST_500,
                "Invest 500",
                DESCRIPTION_INVEST_500
        );
    }

}