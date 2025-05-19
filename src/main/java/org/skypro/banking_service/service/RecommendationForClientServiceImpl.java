package org.skypro.banking_service.service;

import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.dto.RecommendationDTO;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.repositories.postgres.repository.QueryRepository;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.DimanicRule;
import org.skypro.banking_service.service.ruleSystem.statickRulesSystem.rules.StaticRule;
import org.skypro.banking_service.service.statistics.MonitoringStatistics;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис реализации рекомендаций клиенту по новым банковским продуктам.
 * Объединяет статические и динамические правила для определения подходящих продуктов.
 */
@Service
public class RecommendationForClientServiceImpl implements RecommendationForClientService {

    private final List<StaticRule> staticRules;
    private final DimanicRule dimanicRule;
    private final UserTransactionRepository userTransactionRepository;
    private final QueryRepository queryRepository;
    private final MonitoringStatistics monitoringStatistics;

    public RecommendationForClientServiceImpl(List<StaticRule> staticRules, DimanicRule dimanicRule, UserTransactionRepository userTransactionRepository, QueryRepository queryRepository, MonitoringStatistics monitoringStatistics) {
        this.staticRules = staticRules;
        this.dimanicRule = dimanicRule;
        this.userTransactionRepository = userTransactionRepository;
        this.queryRepository = queryRepository;
        this.monitoringStatistics = monitoringStatistics;
    }


    /**
     * Возвращает рекомендации по новым банковским продуктам на основе как статических, так и динамических правил.
     * Метод предварительно проверяет наличие пользователя в системе.
     */
    @Override
    public RecommendationResponse getRecommendationsForClient(UUID userId) {
        //Валидация данных
        validateUserExists(userId);

        List<RecommendationDTO> result = new ArrayList<>();
        result.addAll(getStaticRecommendations(userId));
        result.addAll(getDynamicRecommendations(userId));

        return new RecommendationResponse(userId, result);
    }

    /**
     * Внутренний метод для проверки валидности данных: проверяет существование клиента в БД.
     *
     * @param userId - идентификатор клиента.
     * @throws UserNotFoundException если клиент с заданным Id в БД не найдет.
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

    private List<RecommendationDTO> getStaticRecommendations(UUID userId) {
        List<RecommendationDTO> recommend = new ArrayList<>();
        for (StaticRule rule : staticRules) {
            rule
                    .checkOutStaticRule(userId)
                    .ifPresent(recommend::add);
        }
        return recommend;
    }

    /**
     * Получает список рекомендаций, основанных на динамических правилах из базы данных.
     * У каждой рекомендации может быть один или несколько связанных запросов (queries).
     * Все условия должны быть выполнены, чтобы рекомендация считалась подходящей.
     */
    private List<RecommendationDTO> getDynamicRecommendations(UUID userId) {
        List<RecommendationDTO> validRecommendations = new ArrayList<>();
        Map<Recommendation, List<QueryRules>> queriesByRecommendation = groupQueriesByRecommendation();

        for (Map.Entry<Recommendation, List<QueryRules>> entry : queriesByRecommendation.entrySet()) {
            Recommendation recommendation = entry.getKey();
            List<QueryRules> queries = entry.getValue();

            if (allConditionsAreSatisfied(queries, userId)) {
                validRecommendations.add(convertToDto(recommendation));

                monitoringStatistics.incrementCounter(recommendation.getProductId());
            }
        }
        return validRecommendations;
    }

    /**
     * Группирует все запросы (queries) из БД по объектам.
     */
    private Map<Recommendation, List<QueryRules>> groupQueriesByRecommendation() {
        List<QueryRules> allQueries = queryRepository.findAll();
        Map<Recommendation, List<QueryRules>> grouped = new HashMap<>();

        for (QueryRules queryRules : allQueries) {
            Recommendation recommendation = queryRules.getRecommendation();
            grouped.computeIfAbsent(recommendation, k -> new ArrayList<>()).add(queryRules);
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
    private boolean allConditionsAreSatisfied(List<QueryRules> queries, UUID userId) {
        for (QueryRules queryRules : queries) {
            if (!dimanicRule.checkOutDinamicRule(queryRules, userId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Преобразует сущность {@link Recommendation} в DTO {@link RecommendationDTO}.
     * Используется для возврата клиенту только нужных данных.
     */
    private RecommendationDTO convertToDto(Recommendation recommendation) {
        return new RecommendationDTO(
                recommendation.getProductName(),
                recommendation.getProductId().toString(),
                recommendation.getProductText()
        );
    }
}
