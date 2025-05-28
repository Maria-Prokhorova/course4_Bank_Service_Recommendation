package org.skypro.banking_service.service.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.model.Statistics;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationRepository;
import org.skypro.banking_service.repositories.postgres.repository.StatisticsRepository;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MonitoringStatisticsImplTest {

    private final Recommendation first = new Recommendation();

    private final Recommendation second = new Recommendation();
    private final List<Recommendation> testListRecommendations = new ArrayList<Recommendation>();

    private final List<Recommendation> emptyListRecommendations = new ArrayList<Recommendation>();

    @Mock
    RecommendationRepository recommendationRepository;

    @Mock
    StatisticsRepository statisticsRepository;

    @InjectMocks
    MonitoringStatisticsImpl monitoringStatistics;

    @BeforeEach
    void setUp() {
        first.setId(1L);
        first.setProductId(UUID.randomUUID());
        first.setProductName("One");
        first.setProductText("text");

        second.setId(2L);
        second.setProductId(UUID.randomUUID());
        second.setProductName("Two");
        second.setProductText("nothing");

        testListRecommendations.add(first);
        testListRecommendations.add(second);
    }

    @Test
    void shouldReturnResultOfGetAllRulesIDsWhenSuccessful() {
        when(recommendationRepository.findAll()).thenReturn(testListRecommendations);

        List<UUID> allRuleIDs = monitoringStatistics.getAllRuleIDs();
        assertEquals(2, allRuleIDs.size());

    }

    @Test
    void shouldReturnResultOfGetAllRulesIDsWhenIsEmpty() {
        when(recommendationRepository.findAll()).thenReturn(emptyListRecommendations);

        List<UUID> allRuleIDs = monitoringStatistics.getAllRuleIDs();
        assertEquals(0, allRuleIDs.size());
    }

    @Test
    void shouldReturnResultOfIncrementCounterWhenRuleDoesNotExistYet() {
        UUID ruleID = UUID.randomUUID();
        when(statisticsRepository.existsById(ruleID)).thenReturn(false);

        monitoringStatistics.incrementCounter(ruleID);

        verify(statisticsRepository).existsById(ruleID);
        verify(statisticsRepository).save(any(Statistics.class));
        verify(statisticsRepository).incrementCounter(ruleID);
    }

    @Test
    void shouldReturnResultOfIncrementCounterWhenRuleAlreadyExists() {
        UUID ruleId = UUID.randomUUID();
        when(statisticsRepository.existsById(ruleId)).thenReturn(true);

        monitoringStatistics.incrementCounter(ruleId);

        verify(statisticsRepository).existsById(ruleId);
        verify(statisticsRepository, never()).save(any());
        verify(statisticsRepository).incrementCounter(ruleId);
    }

    @Test
    void shouldReturnResultOfDeleteRuleIdFromStatistics() {
        UUID ruleId = UUID.randomUUID();

        monitoringStatistics.deleteRuleIdFromStatistics(ruleId);

        verify(statisticsRepository, times(1)).deleteById(ruleId);
    }

}
