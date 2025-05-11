package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.Queries;
import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.repositories.postgres.repository.QueriesRepository;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationsRepository;
import org.skypro.banking_service.service.DynamicRulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DynamicRulesServiceImpl implements DynamicRulesService {

    Logger logger = LoggerFactory.getLogger(DynamicRulesServiceImpl.class);

    private final RecommendationsRepository recommendationsRepository;
    private final QueriesRepository queriesRepository;

    public DynamicRulesServiceImpl(RecommendationsRepository ruleRepository, QueriesRepository queriesRepository) {
        this.recommendationsRepository = ruleRepository;
        this.queriesRepository = queriesRepository;
    }

    @Override
    public Recommendations addRecommendationByRule(Recommendations recommendation) {
        for (Queries queries : recommendation.getQueries()) {
            queries.setRecommendations(recommendation);
        }
        logger.info("Was invoked method for create recommendation.");
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

        for (Queries rule : recommendation.getQueries()) {
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
