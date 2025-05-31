package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.cache.impl.RecommendationCache;
import org.skypro.banking_service.cache.impl.StatisticsCache;
import org.skypro.banking_service.constants.ConstantsForDynamicRules;
import org.skypro.banking_service.dto.StatisticsDTO;
import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.model.Statistics;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationRepository;
import org.skypro.banking_service.repositories.postgres.repository.StatisticsRepository;
import org.skypro.banking_service.service.DynamicRulesService;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.DynamicQueryExecutor;
import org.skypro.banking_service.service.statistics.MonitoringStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с банковскими продуктами, позволяет добавить новый или удалить имеющийся продукт, получить список
 * всех банковских продуктов, а также статистику по срабатыванию правил рекомендаций.
 */
@Service
public class DynamicRulesServiceImpl implements DynamicRulesService {

    Logger logger = LoggerFactory.getLogger(DynamicRulesServiceImpl.class);

    private final StatisticsCache statisticsCache;
    private final RecommendationCache recommendationCache;
    private final RecommendationRepository recommendationRepository;
    private final StatisticsRepository statisticsRepository;
    private final MonitoringStatistics monitoringStatistics;
    private final List<DynamicQueryExecutor> executors;

    public DynamicRulesServiceImpl(StatisticsCache statisticsCache,
                                   RecommendationCache recommendationCache,
                                   RecommendationRepository recommendationRepository,
                                   StatisticsRepository statisticsRepository,
                                   MonitoringStatistics monitoringStatistics,
                                   List<DynamicQueryExecutor> executors) {
        this.statisticsCache = statisticsCache;
        this.recommendationCache = recommendationCache;
        this.recommendationRepository = recommendationRepository;
        this.statisticsRepository = statisticsRepository;
        this.monitoringStatistics = monitoringStatistics;
        this.executors = executors;
    }

    /**
     * Метод добавления нового банковского продукта с динамическим правилом рекомендаций.
     *
     * @param recommendation - банковский продукт с динамическим правилом рекомендаций.
     * @return возвращает добавленных продукт.
     */
    @Override
    public Recommendation addProductWithDynamicRule(Recommendation recommendation) {
        for (QueryRules queries : recommendation.getQueries()) {
            queries.setRecommendation(recommendation);
        }

        //Валидация данных (проверяем корректность введенных данных)
        validateData(recommendation);
        logger.info("Was invoked method for create recommendation.");

        recommendationCache.invalidateAll();
        return recommendationRepository.save(recommendation);
    }

    /**
     * Метод получения списка всех банковских продуктов с динамическими правилами рекомендаций.
     *
     * @return - вернут список рекомендаций, хранящихся в базе данных.
     */
    @Override
    public List<Recommendation> getAllProductsWithDynamicRule() {
        return recommendationRepository.findAll();
    }

    /**
     * Метод удаления банковского продукта с его динамическим правилом рекомендаций по-заданному id.
     *
     * @param productId - id банковского продукта.
     */
    @Override
    public void deleteProductWithDynamicRule(UUID productId) {
        // Валидация данных (проверяем существование в БД продукта с таким id)
        Recommendation recommendation = validateProductId(productId);

        recommendationRepository.delete(recommendation);

        // После удаление продукта из базы, удаляем этот продукт из статистики срабатывания рекомендаций
        monitoringStatistics.deleteRuleIdFromStatistics(recommendation.getProductId());

        recommendationCache.invalidateAll();
        statisticsCache.invalidateAll();

        logger.info("Deleted recommendation with productId: {} and cleared stats for ruleId: {}", productId, recommendation.getId());
    }

    /**
     * Метод получения статистических данных по срабатыванию правил рекомендаций.
     *
     * @return вернет список продуктов с количеством выданных рекомендаций по ним. Если по правилу еще не было ни одного срабатывания,
     * то оно всё равно присутствует в списке со значением 0.
     */
    @Override
    public List<StatisticsDTO> getStatistics() {
        return statisticsCache.get("all_stats", key -> {
            List<UUID> allRuleIDs = monitoringStatistics.getAllRuleIDs();
            List<Statistics> statsFromDb = statisticsRepository.findAll();

            Map<UUID, Long> dbMap = statsFromDb.stream()
                    .collect(Collectors.toMap(Statistics::getRuleId, Statistics::getCount));

            return allRuleIDs.stream()
                    .map(ruleId -> new StatisticsDTO(ruleId, dbMap.getOrDefault(ruleId, 0L)))
                    .collect(Collectors.toList());
        });
    }

    /**
     * Внутренний метод проверки валидности данных: проверяет существование рекомендации с указанным id в БД.
     *
     * @param productId - id рекомендации.
     * @return возвращает искомую рекомендацию, в случае существования указанного id.
     * @throws RecommendationNotFoundException - в случае отсутствия указанного id.
     */
    private Recommendation validateProductId(UUID productId) {
        Recommendation recommend = recommendationRepository.findByProductId(productId);
        if (recommend == null) {
            throw new RecommendationNotFoundException("Продукт с таким id не найден.");
        }
        return recommend;
    }

    /**
     * Внутренний метод проверки валидности данных: проверяет кол-во введенных запросов в динамическом правиле,
     * название запроса, название продукта, название транзакции.
     *
     * @param recommendation - информация о динамическом правиле рекомендации и рекомендуемом продукте.
     * @throws IllegalArgumentException в случае невыполнения, какого-либо из условий
     */
    private void validateData(Recommendation recommendation) {
        Set<QueryRules> query = recommendation.getQueries();

        if (query.size() != 3) {
            throw new IllegalArgumentException("Динамическое правило имеет недопустимое количество запросов.");
        }

        query.forEach(this::validateQueryRule);
    }

    private void validateQueryRule(QueryRules rule) {
        validateQueryType(rule.getQuery());
        List<String> arguments = rule.getArguments();
        validateEnum(arguments.get(0), ConstantsForDynamicRules.TypeProduct.class,
                "Тип продукта имеет недопустимое значение: ");

        if (arguments.size() == 4) {
            validateEnum(arguments.get(1), ConstantsForDynamicRules.TypeTransaction.class,
                    "Тип транзакции имеет недопустимое значение: ");
        }
    }

    private void validateQueryType(String query) {
        boolean hasExecutor = executors.stream()
                .anyMatch(e -> e.checkOutNameQuery(query));

        if (!hasExecutor) {
            throw new IllegalArgumentException("Тип запроса имеет недопустимое значение.");
        }
    }

    private <E extends Enum<E>> void validateEnum(String value, Class<E> enumClass, String errorMessage) {
        try {
            Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(errorMessage + value);
        }
    }

}
