package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.Queries;
import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.repositories.postgres.repository.QueriesRepository;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationsRepository;
import org.skypro.banking_service.service.DinamicRulesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DinamicRulesServiceImpl implements DinamicRulesService {

    private final RecommendationsRepository recommendationsRepository;
    private final QueriesRepository queriesRepository;

    public DinamicRulesServiceImpl(RecommendationsRepository ruleRepository, QueriesRepository queriesRepository) {
        this.recommendationsRepository = ruleRepository;
        this.queriesRepository = queriesRepository;
    }

    @Override
    public Recommendations addRecommendationByRule(Recommendations recommendation) {
        return recommendationsRepository.save(recommendation);
    }

    @Override
    public List<Recommendations> getAllRecommendationByRule() {
        List<Recommendations> listRecommendation = recommendationsRepository.findAll();
        return listRecommendation;
    }

    @Override
    public void deleteRecommendationByRule(UUID productId) {
        Recommendations recommendation = validateId(productId);

        for (Queries rule : recommendation.getRule()) {
            queriesRepository.delete(rule);
        }
        recommendationsRepository.delete(recommendation);

    }

    private Recommendations validateId(UUID productId) {
        Recommendations recommend = recommendationsRepository.findByProductId(productId);
        if (recommend == null) {
            throw new RecommendationNotFoundException("Рекомендации с таким id не найдены.");
        }
        return recommend;
    }
}
