package org.skypro.banking_service.service.Impl;

import org.skypro.banking_service.constants.DinamicRuleConstant;
import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.repositories.postgreSQL.repository.QueryRepository;
import org.skypro.banking_service.repositories.postgreSQL.repository.RecommendationRepository;
import org.skypro.banking_service.service.RecommendationWithRulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationWithRulesServiceImpl implements RecommendationWithRulesService {

    Logger logger = LoggerFactory.getLogger(RecommendationWithRulesServiceImpl.class);

    private final RecommendationRepository recommendationRepository;
    private final QueryRepository queryRepository;

    public RecommendationWithRulesServiceImpl(RecommendationRepository ruleRepository, QueryRepository queryRepository) {
        this.recommendationRepository = ruleRepository;
        this.queryRepository = queryRepository;
    }

    @Override
    public Recommendation addRecommendationByRule(Recommendation recommendation) {
        for (QueryRules query : recommendation.getRule()) {
            query.setRecommendations(recommendation);
        }
        validateData(recommendation);
        logger.info("Was invoked method for create recommendation.");

        return recommendationRepository.save(recommendation);
    }

    @Override
    public List<Recommendation> getAllRecommendationByRule() {
        List<Recommendation> listRecommendation = recommendationRepository.findAll();
        return listRecommendation;
    }

    @Override
    public void deleteRecommendationByRule(UUID productId) {
        Recommendation recommendation = validateId(productId);

        for (QueryRules rule : recommendation.getRule()) {
            queryRepository.delete(rule);
        }
        recommendationRepository.delete(recommendation);

    }

    private Recommendation validateId(UUID productId) {
        Recommendation recommend = recommendationRepository.findByProductId(productId);
        if (recommend == null) {
            throw new RecommendationNotFoundException("Рекомендации с таким id не найдены.");
        }
        return recommend;
    }

    private static void validateData(Recommendation recommendation) {
        List<QueryRules> query = recommendation.getRule();

        // Проверка на количество введенных запросов в динамическом правиле.
        if (query.size()!=3) {
            throw new IllegalArgumentException("Динамическое правило имеет недопустимое количество запросов.");
        }

        //Проверка параметров запроса: название запроса, название продукта, название транзакции.
        query.forEach(q -> {
            try {
                DinamicRuleConstant.TypeQuery.valueOf(q.getQuery());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Название запроса имеет недопустимое значение.");
            }
            String [] arguments = q.getArguments();
            try {
                DinamicRuleConstant.TypeProduct.valueOf(arguments[0]);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Название продукта имеет недопустимое значение.");
            }
            if (arguments.length == 4) {
                try {
                    DinamicRuleConstant.TypeTransaction.valueOf(arguments[1]);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Название транзакции имеет недопустимое значение.");
                }
            }

        });
    }
}
