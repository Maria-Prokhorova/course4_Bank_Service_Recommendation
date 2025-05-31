package org.skypro.banking_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.banking_service.cache.impl.RecommendationCache;
import org.skypro.banking_service.cache.impl.StatisticsCache;
import org.skypro.banking_service.dto.StatisticsDTO;
import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.model.Statistics;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationRepository;
import org.skypro.banking_service.repositories.postgres.repository.StatisticsRepository;
import org.skypro.banking_service.service.impl.DynamicRulesServiceImpl;
import org.skypro.banking_service.service.ruleSystem.dynamicRulesSystem.queries.DynamicQueryExecutor;
import org.skypro.banking_service.service.statistics.MonitoringStatistics;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DynamicRulesServiceImplTest {

    @Mock
    private StatisticsCache statisticsCache;
    @Mock
    private RecommendationCache recommendationCache;
    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private StatisticsRepository statisticsRepository;
    @Mock
    private MonitoringStatistics monitoringStatistics;
    @Mock
    private DynamicQueryExecutor executor;

    @InjectMocks
    private DynamicRulesServiceImpl service;

    private Recommendation recommendation;
    private UUID productId;


    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();

        QueryRules q1 = new QueryRules("TRANSACTION_SUM_COMPARE",
                List.of("CREDIT", "DEPOSIT", "1000", "30"), false, null);
        QueryRules q2 = new QueryRules("TRANSACTION_SUM_COMPARE",
                List.of("CREDIT", "WITHDRAW", "1000", "30"), false, null);
        QueryRules q3 = new QueryRules("TRANSACTION_SUM_COMPARE",
                List.of("CREDIT", "DEPOSIT", "2000", "30"), false, null);

        recommendation = new Recommendation();
        recommendation.setProductId(productId);
        recommendation.setProductName("Product");
        recommendation.setProductText("Description");
        recommendation.setQueries(new HashSet<>(List.of(q1, q2, q3)));

        ReflectionTestUtils.setField(service, "executors", List.of(executor));
    }

    // Тесты для addProductWithDynamicRule
    @Test
    void addProductWithDynamicRule_validRecommendation_savesAndInvalidatesCache() {
        when(executor.checkOutNameQuery(anyString())).thenReturn(true);
        when(recommendationRepository.save(any())).thenReturn(recommendation);

        service = new DynamicRulesServiceImpl(
                statisticsCache,
                recommendationCache,
                recommendationRepository,
                statisticsRepository,
                monitoringStatistics,
                List.of(executor)
        );

        Recommendation result = service.addProductWithDynamicRule(recommendation);

        assertNotNull(result);
        verify(recommendationRepository).save(recommendation);
        verify(recommendationCache).invalidateAll();
    }

    @Test
    void addProductWithDynamicRule_invalidQueryCount_throwsException() {
        recommendation.setQueries(Set.of(new QueryRules("QUERY", List.of("CREDIT"), false, null)));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.addProductWithDynamicRule(recommendation)
        );

        assertEquals("Динамическое правило имеет недопустимое количество запросов.", exception.getMessage());
    }

    @Test
    void addProductWithDynamicRule_invalidQueryType_throwsException() {
        QueryRules invalid1 = new QueryRules("UNKNOWN_QUERY",
                List.of("CREDIT", "DEPOSIT", "1000", "30"), false, null);
        QueryRules invalid2 = new QueryRules("UNKNOWN_QUERY",
                List.of("CREDIT", "WITHDRAW", "2000", "30"), false, null);
        QueryRules invalid3 = new QueryRules("UNKNOWN_QUERY",
                List.of("DEPOSIT", "CREDIT", "1500", "60"), false, null);

        recommendation.setQueries(new HashSet<>(List.of(invalid1, invalid2, invalid3)));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                service.addProductWithDynamicRule(recommendation)
        );

        assertEquals("Тип запроса имеет недопустимое значение.", exception.getMessage());
    }

    @Test
    void deleteProductWithDynamicRule_existingProduct_removesFromDbAndInvalidatesCaches() {
        when(recommendationRepository.findByProductId(productId)).thenReturn(recommendation);

        service.deleteProductWithDynamicRule(productId);

        verify(recommendationRepository).delete(recommendation);
        verify(recommendationCache).invalidateAll();
        verify(statisticsCache).invalidateAll();
        verify(monitoringStatistics).deleteRuleIdFromStatistics(productId);
    }

    @Test
    void deleteProductWithDynamicRule_notFound_throwsException() {
        when(recommendationRepository.findByProductId(productId)).thenReturn(null);

        assertThrows(RecommendationNotFoundException.class, () ->
                service.deleteProductWithDynamicRule(productId)
        );
    }

    @Test
    void getAllProductsWithDynamicRule_returnsList() {
        List<Recommendation> recommendations = List.of(recommendation);
        when(recommendationRepository.findAll()).thenReturn(recommendations);

        List<Recommendation> result = service.getAllProductsWithDynamicRule();

        assertEquals(1, result.size());
        assertEquals("Product", result.get(0).getProductName());
    }

    @Test
    void getStatistics_returnsMergedStatistics() {
        UUID ruleId = UUID.randomUUID();
        when(monitoringStatistics.getAllRuleIDs()).thenReturn(List.of(ruleId));
        when(statisticsRepository.findAll()).thenReturn(List.of(new Statistics(ruleId, 5L)));

        when(statisticsCache.get(anyString(), any()))
                .thenAnswer(invocation -> {
                    Supplier<List<StatisticsDTO>> supplier = invocation.getArgument(1);
                    return supplier.get();
                });
        List<StatisticsDTO> result = service.getStatistics();
        assertEquals(1, result.size());
        assertEquals(ruleId, result.get(0).ruleId());
        assertEquals(5L, result.get(0).count());
    }

}