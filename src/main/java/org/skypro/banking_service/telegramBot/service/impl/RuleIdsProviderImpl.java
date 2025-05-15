package org.skypro.banking_service.telegramBot.service.impl;

import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationsRepository;
import org.skypro.banking_service.telegramBot.service.RuleIdsProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RuleIdsProviderImpl implements RuleIdsProvider {

    private final RecommendationsRepository recommendationsRepository;

    public RuleIdsProviderImpl(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public List<UUID> getAllRuleProductIds() {
        return recommendationsRepository.findAll()
                .stream()
                .map(Recommendations::getProductId)
                .collect(Collectors.toList());
    }
}
