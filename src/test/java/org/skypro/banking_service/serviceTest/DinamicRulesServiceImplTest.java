package org.skypro.banking_service.serviceTest;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.Queries;
import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.repositories.postgres.repository.QueriesRepository;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationsRepository;
import org.skypro.banking_service.service.impl.DinamicRulesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.skypro.banking_service.constants.ProductConstants.*;

@SpringBootTest
public class DinamicRulesServiceImplTest {

    @Autowired
    DinamicRulesServiceImpl dinamicRulesService;

    @Autowired
    RecommendationsRepository recommendationsRepository;

    @Test
    public void shouldReturnAddRecommendationByRulesSuccessful() {
        Queries query = new Queries();
        List<Queries> queries = new ArrayList<>(List.of(query));
        Recommendations recommendations = new Recommendations(
                UUID.fromString(PRODUCT_ID_TOP_SAVING),
                PRODUCT_NAME_TOP_SAVING,
                DESCRIPTION_TOP_SAVING,
                queries);

        Recommendations result = dinamicRulesService.addRecommendationByRule(recommendations);
        org.junit.jupiter.api.Assertions.assertEquals(recommendations, result);
        recommendationsRepository.delete(recommendations);
    }

    @Test
    public void shouldReturnGetAllRecommendationByRule() {
        Assertions.assertThat(dinamicRulesService.getAllRecommendationByRule()).hasSize(3);
    }

    @Test
    public void shouldReturnDeleteRecommendationByRuleThrowsRecommendationNotFoundException() {

        org.junit.jupiter.api.Assertions.assertThrows(
                RecommendationNotFoundException.class,
                () -> dinamicRulesService.deleteRecommendationByRule(UUID.fromString(PRODUCT_ID_SIMPLE_CREDIT)));
    }

}
