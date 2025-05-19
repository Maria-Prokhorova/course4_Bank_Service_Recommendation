package org.skypro.banking_service.service;

import org.skypro.banking_service.constants.ConstantsForDynamicRules;
import org.skypro.banking_service.exception.QueryEvaluationException;
import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.model.Statistics;
import org.skypro.banking_service.dto.StatisticsDTO;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationRepository;
import org.skypro.banking_service.repositories.postgres.repository.StatisticsRepository;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.DimanicQueryExecutor;
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

    private final RecommendationRepository recommendationRepository;
    private final StatisticsRepository statisticsRepository;
    private final MonitoringStatistics monitoringStatistics;
    private final List<DimanicQueryExecutor> executors;

    public DynamicRulesServiceImpl(RecommendationRepository recommendationRepository,
                                   StatisticsRepository statisticsRepository, MonitoringStatistics monitoringStatistics, List<DimanicQueryExecutor> executors) {
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

        //Валидация данных
        validateData(recommendation);
        logger.info("Was invoked method for create recommendation.");
        return recommendationRepository.save(recommendation);
    }

    /**
     * Метод получения списка всех банковских продуктов с динамическими правилами рекомендаций.
     *
     * @return - вернут список рекомендаций, хранящихся в базе данных.
     */
    @Override
    public List<Recommendation> getAllProductsWithDynamicRule() {
        List<Recommendation> listRecommendation = recommendationRepository.findAll();
        return listRecommendation;
    }

    /**
     * Метод удаления банковского продукта с его динамическим правилом рекомендаций по заданному id.
     *
     * @param productId - id банковского продукта.
     */
    @Override
    public void deleteProductWithDynamicRule(UUID productId) {
        // Валидация данных
        Recommendation recommendation = validateProductId(productId);

        recommendationRepository.delete(recommendation);
        monitoringStatistics.deleteRuleIdFromStatistics(recommendation.getProductId());

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
        List<UUID> allRuleIDs = monitoringStatistics.getAllRuleIDs();
        List<Statistics> statsFromDb = statisticsRepository.findAll();

        Map<UUID, Long> dbMap = statsFromDb.stream()
                .collect(Collectors.toMap(Statistics::getRuleId, Statistics::getCount));

        return allRuleIDs.stream()
                .map(ruleId -> new StatisticsDTO(ruleId, dbMap.getOrDefault(ruleId, 0L)))
                .collect(Collectors.toList());
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
            throw new RecommendationNotFoundException("Рекомендации с таким id не найдены.");
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

        // Проверка на количество введенных запросов в динамическом правиле.
        if (query.size() != 3) {
            throw new IllegalArgumentException("Динамическое правило имеет недопустимое количество запросов.");
        }

        //Проверка параметров запроса: тип запроса, тип продукта, тип транзакции.
        query.forEach(q -> {
            try {
                // Находим подходящий обработчик запросов (система поддерживает 4 типа запросов)
                DimanicQueryExecutor executor = executors.stream()
                        .filter(e -> e.checkOutNameQuery(q.getQuery()))
                        .findFirst()
                        .orElseThrow(() -> new QueryEvaluationException("No executor for query: " + q.getQuery()));

            } catch (QueryEvaluationException e) {
                throw new IllegalArgumentException("Тип запроса имеет недопустимое значение.");
            }
            List<String> arguments = q.getArguments();
            try {
                ConstantsForDynamicRules.TypeProduct.valueOf(arguments.get(0));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Тип продукта имеет недопустимое значение.");
            }
            if (arguments.size() == 4) {
                try {
                    ConstantsForDynamicRules.TypeTransaction.valueOf(arguments.get(1));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Тип транзакции имеет недопустимое значение.");
                }
            }
        });
    }
}
