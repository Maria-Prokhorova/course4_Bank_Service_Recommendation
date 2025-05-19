package org.skypro.banking_service.serviceTest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.repositories.postgres.repository.QueryRepository;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationRepository;
import org.skypro.banking_service.service.DynamicRulesServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DynamicStaticQueryRulesExecutorImplTest {

    private final Set<QueryRules> queries = Set.of(new QueryRules());

    private final Recommendation recFirst = new Recommendation(
            UUID.randomUUID(),
            "DEBIT",
            "text",
            queries
    );
    private final Recommendation recSecond = new Recommendation(
            UUID.randomUUID(),
            "DEBIT",
            "text",
            new HashSet<QueryRules>()
    );
    private final List<Recommendation> recommendationsList = List.of(recFirst, recSecond);

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private QueryRepository queryRepository;

    @InjectMocks
    DynamicRulesServiceImpl dynamicRulesServiceImpl;

    @Test
    void shouldReturnResultOfAddRecommendationByRuleSuccessfully() {
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(recFirst);
        assertNotNull(dynamicRulesServiceImpl.addProductWithDynamicRule(recFirst));
    }

    @Test
    void shouldReturnResultOfGetAllRecommendationByRule() {
        when(recommendationRepository.findAll()).thenReturn(recommendationsList);
        assertEquals(recommendationsList, dynamicRulesServiceImpl.getAllProductsWithDynamicRule());
    }

    @Test
    void shouldReturnResultOfDeleteRecommendationByRuleThrowsRecommendationNotFoundException() {
        assertThrows(
                RecommendationNotFoundException.class,
                () -> dynamicRulesServiceImpl.deleteProductWithDynamicRule(recFirst.getProductId()));
    }

    @Test
    void shouldReturnResultOfDeleteRecommendationByRule() {
        when(recommendationRepository.findByProductId(recFirst.getProductId())).thenReturn(recFirst);
        dynamicRulesServiceImpl.deleteProductWithDynamicRule(recFirst.getProductId());

        verify(queryRepository).delete(any(QueryRules.class));
        verify(recommendationRepository).delete(recFirst);
    }

}