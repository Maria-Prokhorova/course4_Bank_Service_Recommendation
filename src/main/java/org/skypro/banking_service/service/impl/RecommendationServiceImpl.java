package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.model.Queries;
import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.model.dto.RecommendationDto;
import org.skypro.banking_service.model.dto.RecommendationResponse;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.repositories.postgres.repository.QueriesRepository;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationsRepository;
import org.skypro.banking_service.ruleSystem.dynamicRulesSystem.serviceQuery.RuleQueryService;
import org.skypro.banking_service.ruleSystem.statickRulesSystem.RecommendationRule;
import org.skypro.banking_service.service.RecommendationService;
import org.skypro.banking_service.telegramBot.service.RuleStatService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис реализации рекомендаций клиенту по новым банковским продуктам.
 * Объединяет статические и динамические правила для определения подходящих продуктов.
 */
@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationsRepository recommendationsRepository;
    private final RuleStatService ruleStatService;
    private final List<RecommendationRule> staticRules;
    private final QueriesRepository queriesRepository;
    private final RuleQueryService ruleQueryService;
    private final UserTransactionRepository userTransactionRepository;

    public RecommendationServiceImpl(RecommendationsRepository recommendationsRepository,
                                     RuleStatService ruleStatService,
                                     List<RecommendationRule> staticRules,
                                     QueriesRepository queriesRepository,
                                     RuleQueryService ruleQueryService,
                                     UserTransactionRepository userTransactionRepository) {
        this.recommendationsRepository = recommendationsRepository;
        this.ruleStatService = ruleStatService;
        this.staticRules = staticRules;
        this.queriesRepository = queriesRepository;
        this.ruleQueryService = ruleQueryService;
        this.userTransactionRepository = userTransactionRepository;
    }

    /**
     * Возвращает рекомендации по новым банковским продуктам на основе как статических, так и динамических правил.
     * Метод предварительно проверяет наличие пользователя в системе.
     */
    @Override
    public RecommendationResponse getRecommendations(UUID userId) {
        validateUserExists(userId);
        List<RecommendationDto> result = new ArrayList<>();
        result.addAll(getStaticRecommendations(userId));
        result.addAll(getDynamicRecommendations(userId));

        return new RecommendationResponse(userId, result);
    }

    /** Внутренний метод для проверки валидности данных: проверяет существование клиента в БД.
     *
     * @throws UserNotFoundException если клиент с заданным Id в БД не найдет.
     *
     * @param userId - идентификатор клиента.
     */
    private void validateUserExists(UUID userId) {
        if (!userTransactionRepository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * Получает список рекомендаций по статическим правилам (встроенные Java-реализации).
     * Каждое правило проверяет, подходит ли оно данному пользователю, и при успешной проверке
     * возвращает DTO рекомендации.
     */

    private List<RecommendationDto> getStaticRecommendations(UUID userId) {
        List<RecommendationDto> recommend = new ArrayList<>();
        for (RecommendationRule rule : staticRules) {
            rule
                    .checkOut(userId)
                    .ifPresent(recommend::add);
        }
        return recommend;
    }

    /**
     * Получает список рекомендаций, основанных на динамических условиях из базы данных.
     * У каждой рекомендации может быть один или несколько связанных запросов (queries).
     * Все условия должны быть выполнены, чтобы рекомендация считалась подходящей.
     */
    private List<RecommendationDto> getDynamicRecommendations(UUID userId) {
        List<RecommendationDto> validRecommendations = new ArrayList<>();
        Map<Recommendations, List<Queries>> queriesByRecommendation = groupQueriesByRecommendation();

        for (Map.Entry<Recommendations, List<Queries>> entry : queriesByRecommendation.entrySet()) {
            Recommendations recommendation = entry.getKey();
            List<Queries> queries = entry.getValue();

            if (allConditionsAreSatisfied(queries, userId)) {
                validRecommendations.add(convertToDto(recommendation));

                ruleStatService.incrementCounter(recommendation.getProductId());
            }
        }

        return validRecommendations;
    }


    /**
     * Группирует все запросы (queries) из БД по объектам.
     */
    private Map<Recommendations, List<Queries>> groupQueriesByRecommendation() {
        List<Queries> allQueries = queriesRepository.findAll();
        Map<Recommendations, List<Queries>> grouped = new HashMap<>();

        for (Queries query : allQueries) {
            Recommendations recommendation = query.getRecommendations();
            grouped.computeIfAbsent(recommendation, k -> new ArrayList<>()).add(query);
        }

        return grouped;
    }

    /**
     * Проверяет, выполняются ли все условия (queries) динамической рекомендации
     * для конкретного пользователя.
     *
     * @param queries Список условий.
     * @param userId  Идентификатор пользователя.
     * @return true, если все условия удовлетворены; false — иначе.
     */
    private boolean allConditionsAreSatisfied(List<Queries> queries, UUID userId) {
        for (Queries query : queries) {
            if (!ruleQueryService.ruleQuery(query, userId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Преобразует сущность {@link Recommendations} в DTO {@link RecommendationDto}.
     * Используется для возврата клиенту только нужных данных.
     */
    private RecommendationDto convertToDto(Recommendations recommendation) {
        return new RecommendationDto(
                recommendation.getProductName(),
                recommendation.getProductId().toString(),
                recommendation.getProductText()
        );
    }

    @CacheEvict(value = "recommendations", allEntries = true)
    @Override
    public void clearCache() {
        // Spring автоматически очистит кеш благодаря аннотации
    }

}
