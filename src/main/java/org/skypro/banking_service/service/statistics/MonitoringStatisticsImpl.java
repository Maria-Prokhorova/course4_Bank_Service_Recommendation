package org.skypro.banking_service.service.statistics;

import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.model.Statistics;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationRepository;
import org.skypro.banking_service.repositories.postgres.repository.StatisticsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MonitoringStatisticsImpl implements MonitoringStatistics {

    private final RecommendationRepository recommendationRepository;
    private final StatisticsRepository statisticsRepository;

    public MonitoringStatisticsImpl(RecommendationRepository recommendationRepository, StatisticsRepository statisticsRepository) {
        this.recommendationRepository = recommendationRepository;
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * Метод получение списка всех идентификаторов существующих в базе правил.
     *
     * @return - список идентификаторов правил.
     */
    @Override
    public List<UUID> getAllRuleIDs() {
        return recommendationRepository.findAll()
                .stream()
                .map(Recommendation::getProductId)
                .collect(Collectors.toList());
    }

    /**
     * Метод увеличивает число срабатываний заданного правила.
     *
     * @param ruleId - id правила.
     */
    @Transactional
    @Override
    public void incrementCounter(UUID ruleId) {
        if (!statisticsRepository.existsById(ruleId)) {
            statisticsRepository.save(new Statistics(ruleId, 0L));
        }
        statisticsRepository.incrementCounter(ruleId);
    }

    /**
     * Метод удаляет правила из мониторинга статистики по его id.
     *
     * @param ruleId - id правила.
     */
    @Override
    public void deleteRuleIdFromStatistics(UUID ruleId) {
        statisticsRepository.deleteById(ruleId);
    }
}
