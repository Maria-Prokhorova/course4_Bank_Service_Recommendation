package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.Queries;
import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.repositories.postgres.repository.QueriesRepository;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationsRepository;
import org.skypro.banking_service.service.DynamicRulesService;
import org.skypro.banking_service.telegramBot.service.RuleStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DynamicRulesServiceImpl implements DynamicRulesService {

    Logger logger = LoggerFactory.getLogger(DynamicRulesServiceImpl.class);

    private final RuleStatService ruleStatService;
    private final RecommendationsRepository recommendationsRepository;
    private final QueriesRepository queriesRepository;

    public DynamicRulesServiceImpl(RuleStatService ruleStatService,
                                   RecommendationsRepository recommendationsRepository,
                                   QueriesRepository queriesRepository) {
        this.ruleStatService = ruleStatService;
        this.recommendationsRepository = recommendationsRepository;
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
        Recommendations recommendation = validateProductId(productId);

        recommendationsRepository.delete(recommendation);
        ruleStatService.deleteStat(recommendation.getProductId());

        logger.info("Deleted recommendation with productId: {} and cleared stats for ruleId: {}", productId, recommendation.getId());
    }

    private Recommendations validateProductId(UUID productId) {
        Recommendations recommend = recommendationsRepository.findByProductId(productId);
        if (recommend == null) {
            throw new RecommendationNotFoundException("Рекомендации с таким id не найдены.");
        }
        return recommend;
    }
}
