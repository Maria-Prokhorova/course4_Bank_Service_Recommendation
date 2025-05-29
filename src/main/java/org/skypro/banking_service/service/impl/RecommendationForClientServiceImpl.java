package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.dto.RecommendationDTO;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.repositories.postgres.repository.QueryRepository;
import org.skypro.banking_service.service.RecommendationForClientService;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.DynamicRule;
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
    private final DynamicRule dynamicRule;
    private final UserTransactionRepository userTransactionRepository;
    private final QueryRepository queryRepository;
    private final MonitoringStatistics monitoringStatistics;

    public RecommendationForClientServiceImpl(List<StaticRule> staticRules, DynamicRule dynamicRule, UserTransactionRepository userTransactionRepository, QueryRepository queryRepository, MonitoringStatistics monitoringStatistics) {
        this.staticRules = staticRules;
        this.dynamicRule = dynamicRule;
        this.userTransactionRepository = userTransactionRepository;
        this.queryRepository = queryRepository;
        this.monitoringStatistics = monitoringStatistics;
    }

    /**
     * Метод позволяет получить рекомендации банковских продуктов для заданного клиента.
     * Рекомендации формируются по продуктам со статическими и динамическими правилами.
     * Метод предварительно проверяет наличие клиента в БД.
     *
     * @param userId - id клиента.
     * @return - список рекомендаций.
     */
    @Override
    public RecommendationResponse getRecommendationsForClient(UUID userId) {
        //Валидация данных (проверка id клиента)
        validateUserExists(userId);

        List<RecommendationDTO> result = new ArrayList<>();
        result.addAll(getStaticRecommendations(userId));
        result.addAll(getDynamicRecommendations(userId));

        return new RecommendationResponse(userId, result);
    }

    /**
     * Внутренний метод для проверки валидности данных: проверяет существование клиента в БД.
     *
     * @param userId - id клиента.
     * @throws UserNotFoundException, если клиент с заданным Id в БД не найдет.
     */
    private void validateUserExists(UUID userId) {
        if (!userTransactionRepository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * Внутренний метод, который позволяет получить рекомендации продуктов со статическими правилами для заданного клиента.
     * Выполняется проверка по каждому продукту со статическим правилом (всего в системе описано три продукта:
     * Invest500, SimpleCredit, TopSaving), в случае успешной проверки продукт попадает в рекомендации.
     *
     * @param userId - id клиента.
     * @return - список рекомендаций.
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
     * Внутренний метод, который позволяет получить рекомендации продуктов с динамическими правилами для заданного клиента.
     * В динамическом правиле каждого продукта есть три запроса (queries). Для того, что бы продукт попал в
     * рекомендацию должны выполняться условия всех запросов.
     *
     * @param userId id клиента.
     * @return - список рекомендаций.
     */
    private List<RecommendationDTO> getDynamicRecommendations(UUID userId) {
        List<RecommendationDTO> validRecommendations = new ArrayList<>();
        // Создаем мапу <продукт, его запросы>
        Map<Recommendation, List<QueryRules>> queriesByRecommendation = groupQueriesWithProduct();

        // В цикле проходимся по всем элементам мапы
        for (Map.Entry<Recommendation, List<QueryRules>> entry : queriesByRecommendation.entrySet()) {
            Recommendation recommendation = entry.getKey();
            List<QueryRules> queries = entry.getValue();

            // Проверяем выполнения условий для всех запросов продукта
            if (checkOutAllQueriesByDymanicRule(queries, userId)) {
                validRecommendations.add(convertToDto(recommendation));

                // Увеличиваем счетчик срабатывания рекомендаций для статистики
                monitoringStatistics.incrementCounter(recommendation.getProductId());
            }
        }
        return validRecommendations;
    }

    /**
     * Внутренний метод, который позволяет сгруппировать все запросы (queries) с соответствующими им продуктами в единый объект.
     *
     * @return - возвращается мапу <продукт, его запросы>.
     */
    private Map<Recommendation, List<QueryRules>> groupQueriesWithProduct() {
        // Получаем все запросы из БД
        List<QueryRules> allQueries = queryRepository.findAll();
        Map<Recommendation, List<QueryRules>> grouped = new HashMap<>();

        // Проходимся в цикле по всем запросам и сохраняем их в мапу, где ключом выступает продукт, а значением список его запросов
        for (QueryRules queryRules : allQueries) {
            Recommendation recommendation = queryRules.getRecommendation();
            grouped.computeIfAbsent(recommendation, k -> new ArrayList<>()).add(queryRules);
        }
        return grouped;
    }

    /**
     * Внутренний метод, который выполняет проверку соблюдения условий всех запросов (queries) в динамическом правиле
     * для конкретного клиента.
     *
     * @param queries - список запросов.
     * @param userId  - id клиента.
     * @return булевое значение: true, если все условия удовлетворены; false — иначе.
     */
    private boolean checkOutAllQueriesByDymanicRule(List<QueryRules> queries, UUID userId) {
        // Проходимся в цикле по всем запросам. Для каждого запроса вызываем метод, который сначала подбирает нужный
        // обработчик запроса, а затем проверяет выполнение условий запроса
        for (QueryRules queryRules : queries) {
            if (!dynamicRule.checkOutDinamicRule(queryRules, userId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Внутренний метод, который преобразует сущность {@link Recommendation} в DTO {@link RecommendationDTO}.
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
