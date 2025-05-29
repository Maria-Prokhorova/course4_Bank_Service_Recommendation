package org.skypro.banking_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.banking_service.constants.ConstantsForDynamicRules;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DynamicRulesServiceImplTest {

    private final Recommendation first = new Recommendation(
            UUID.randomUUID(),
            "DEBIT",
            "text",
            new HashSet<>(Set.of())
    );

    private final Recommendation recSecond = new Recommendation(
            UUID.randomUUID(),
            "DEBIT",
            "text",
            new HashSet<QueryRules>()
    );


    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private MonitoringStatistics monitoringStatistics;

    @Mock
    private DynamicQueryExecutor queryExecutor1;

    @Mock
    private DynamicQueryExecutor queryExecutor2;

    @InjectMocks
    private DynamicRulesServiceImpl service;

    private UUID testProductId;
    private Recommendation testRecommendation;
    private List<QueryRules> testQueries;
    private List<Recommendation> recommendationsList;

    @BeforeEach
    void setUp() {
        testProductId = UUID.randomUUID();

        testRecommendation = new Recommendation();
        testRecommendation.setProductId(testProductId);
        testRecommendation.setProductName("Product Name");
        testRecommendation.setProductText("text");

        testQueries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            QueryRules query = new QueryRules(
                    "queryType" + (i+1),
                    Arrays.asList(
                            ConstantsForDynamicRules.TypeProduct.CREDIT.name(),
                            ConstantsForDynamicRules.TypeTransaction.DEPOSIT.name()),
                    false,
                    null
            );
            testQueries.add(query);


            //query.setRecommendation(testRecommendation);
        }

        testRecommendation.setQueries(new HashSet<>(testQueries));

        List<DynamicQueryExecutor> executors = Arrays.asList(queryExecutor1, queryExecutor2);
        service = new DynamicRulesServiceImpl(
                recommendationRepository,
                statisticsRepository,
                monitoringStatistics,
                executors
        );

        recommendationsList = List.of(first, recSecond);
    }

    @Test
    void shouldReturnResultOfAddProductWithDynamicRule_ValidInput_Success() {
        // Подготовка моков
        when(queryExecutor1.checkOutNameQuery(anyString())).thenReturn(true);
        when(recommendationRepository.save(testRecommendation)).thenReturn(testRecommendation);

        // Выполнение метода
        Recommendation result = service.addProductWithDynamicRule(testRecommendation);

        // Проверка результата
        assertNotNull(result);
        assertEquals(testProductId, result.getProductId());
    }

    @Test
    void shouldReturnResultOfAddProductWithDynamicRule_InvalidCountQuery_ThrowException() {
        //подготовка, где оставляем только один запрос
        testRecommendation.setQueries(new HashSet<>(testQueries.subList(0, 1)));

        //Проверка выброса исключения
        assertThrows(IllegalArgumentException.class, () -> service.addProductWithDynamicRule(testRecommendation));
    }

    @Test
    void shouldReturnResultOfAddProductWithDynamicRule_InvalidProductType_ThrowsException() {
        testQueries.get(0).setArguments(Arrays.asList("INVALID_TYPE", "PAYMENT", "1000", "30"));

        // Выполнение и проверка
        assertThrows(IllegalArgumentException.class,
                () -> service.addProductWithDynamicRule(testRecommendation));
    }

    @Test
    void shouldReturnResultOfAddProductWithDynamicRule_InvalidQueryType_ThrowsException() {
        // Подготовка
        when(queryExecutor1.checkOutNameQuery(anyString())).thenReturn(false);
        when(queryExecutor2.checkOutNameQuery(anyString())).thenReturn(false);

        // Выполнение и проверка
        assertThrows(IllegalArgumentException.class,
                () -> service.addProductWithDynamicRule(testRecommendation));
    }

    @Test
    void shouldReturnResultOfGetAllRecommendationByRule() {
        when(recommendationRepository.findAll()).thenReturn(recommendationsList);
        assertEquals(recommendationsList, service.getAllProductsWithDynamicRule());
        assertEquals(2, service.getAllProductsWithDynamicRule().size());
    }

    @Test
    void shouldReturnResultOfDeleteProductWithDynamicRule_ValidId_Success() {
        when(recommendationRepository.findByProductId(testProductId)).thenReturn(testRecommendation);

        service.deleteProductWithDynamicRule(testProductId);

        verify(recommendationRepository).delete(testRecommendation);
        verify(monitoringStatistics).deleteRuleIdFromStatistics(testProductId);
    }

    @Test
    void deleteProductWithDynamicRule_InvalidId_ThrowsException() {
        when(recommendationRepository.findByProductId(testProductId)).thenReturn(null);

        assertThrows(RecommendationNotFoundException.class,
                () -> service.deleteProductWithDynamicRule(testProductId));
    }

    @Test
    void shouldReturnResultOfGetStatistics_ReturnsCorrectData() {
        UUID ruleId1 = UUID.randomUUID();
        UUID ruleId2 = UUID.randomUUID();

        List<UUID> allRuleIds = Arrays.asList(ruleId1, ruleId2);
        when(monitoringStatistics.getAllRuleIDs()).thenReturn(allRuleIds);

        Statistics stat1 = new Statistics();
        stat1.setRuleId(ruleId1);
        stat1.setCount(5L);
        when(statisticsRepository.findAll()).thenReturn(Collections.singletonList(stat1));

        List<StatisticsDTO> result = service.getStatistics();

        assertEquals(2, result.size());

        Optional<StatisticsDTO> dto1 = result.stream().filter(d -> d.ruleId().equals(ruleId1)).findFirst();
        assertTrue(dto1.isPresent());
        assertEquals(5L, dto1.get().count());

        Optional<StatisticsDTO> dto2 = result.stream().filter(d -> d.ruleId().equals(ruleId2)).findFirst();
        assertTrue(dto2.isPresent());
        assertEquals(0L, dto2.get().count());
    }

    @Test
    void shouldReturnResultOfGetStatistics_NoStatistics_ReturnsZeroCount() {
        UUID ruleId1 = UUID.randomUUID();
        when(monitoringStatistics.getAllRuleIDs()).thenReturn(Collections.singletonList(ruleId1));
        when(statisticsRepository.findAll()).thenReturn(Collections.emptyList());

        List<StatisticsDTO> result = service.getStatistics();

        assertEquals(1, result.size());
        assertEquals(ruleId1, result.get(0).ruleId());
        assertEquals(0L, result.get(0).count());
    }

}