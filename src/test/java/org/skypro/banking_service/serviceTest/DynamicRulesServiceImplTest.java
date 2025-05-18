package org.skypro.banking_service.serviceTest;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.Queries;
import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.repositories.postgres.repository.QueriesRepository;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationsRepository;
import org.skypro.banking_service.service.impl.DynamicRulesServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DynamicRulesServiceImplTest {

    private final Set<Queries> queries = Set.of(new Queries());

    private final Recommendations recFirst = new Recommendations(
            UUID.randomUUID(),
            "DEBIT",
            "text",
            queries
    );
    private final Recommendations recSecond = new Recommendations(
            UUID.randomUUID(),
            "DEBIT",
            "text",
            new HashSet<Queries>()
    );
    private final List<Recommendations> recommendationsList = List.of(recFirst, recSecond);

    @Mock
    private RecommendationsRepository recommendationsRepository;

    @Mock
    private QueriesRepository queriesRepository;

    @InjectMocks
    DynamicRulesServiceImpl dynamicRulesServiceImpl;

    @Test
    void shouldReturnResultOfAddRecommendationByRuleSuccessfully() {
        when(recommendationsRepository.save(any(Recommendations.class))).thenReturn(recFirst);
        assertNotNull(dynamicRulesServiceImpl.addRecommendationByRule(recFirst));
    }

    @Test
    void shouldReturnResultOfGetAllRecommendationByRule() {
        when(recommendationsRepository.findAll()).thenReturn(recommendationsList);
        assertEquals(recommendationsList, dynamicRulesServiceImpl.getAllRecommendationByRule());
    }

    @Test
    void shouldReturnResultOfDeleteRecommendationByRuleThrowsRecommendationNotFoundException() {
        assertThrows(
                RecommendationNotFoundException.class,
                () -> dynamicRulesServiceImpl.deleteRecommendationByRule(recFirst.getProductId()));
    }

    @Test
    void shouldReturnResultOfDeleteRecommendationByRule() {
        when(recommendationsRepository.findByProductId(recFirst.getProductId())).thenReturn(recFirst);
        dynamicRulesServiceImpl.deleteRecommendationByRule(recFirst.getProductId());

        verify(queriesRepository).delete(any(Queries.class));
        verify(recommendationsRepository).delete(recFirst);
    }

}