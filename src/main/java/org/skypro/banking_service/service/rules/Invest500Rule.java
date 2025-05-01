package org.skypro.banking_service.service.rules;

import org.skypro.banking_service.repository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Invest500Rule implements RecommendationRule {

    private final RecommendationRepository recommendationRepository;

    public Invest500Rule(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    public Boolean getProduct(UUID userId) {
        boolean requirementFirst = recommendationRepository.existsUserProductByType(userId, "DEBIT");
        boolean requirementSecond = !(recommendationRepository.existsUserProductByType(userId, "INVEST"));
        boolean requirementThird = recommendationRepository.findTotalDepositByUserIdAndProductType(userId, "SAVING") > 1000;

        return requirementFirst && requirementSecond && requirementThird;
    }

}