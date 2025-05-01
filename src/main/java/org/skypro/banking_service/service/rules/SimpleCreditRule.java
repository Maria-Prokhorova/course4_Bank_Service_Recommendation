package org.skypro.banking_service.service.rules;

import org.skypro.banking_service.repository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SimpleCreditRule implements RecommendationRule {
    private final RecommendationRepository repository;


    public SimpleCreditRule(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean checkOut(UUID userId) {
        Set<String> products = new HashSet<>(repository.findUsedProductTypesByUserId(userId));
        long deposit = repository.findTotalDepositByUserIdAndProductType(userId, "DEBIT");
        long withdraw = repository.findTotalWithdrawByUserIdAndProductType(userId, "DEBIT");

        if (!products.contains("CREDIT") && deposit > withdraw && withdraw > 100_000) {
            return true;
        } else return false;
    }
}

