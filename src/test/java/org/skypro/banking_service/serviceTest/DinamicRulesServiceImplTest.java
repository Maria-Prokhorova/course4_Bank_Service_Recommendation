package org.skypro.banking_service.serviceTest;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
public class DinamicRulesServiceImplTest {

    @Mock
    RecommendationsRepository recommendationsRepository;

    @Mock
    QueriesRepository queriesRepository;

    @InjectMocks
    DinamicRulesServiceImpl out;

    @Autowired
    DinamicRulesServiceImpl dinamicRulesService;

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
        Assertions.assertThat(result).isEqualTo(recommendations);
    }

    @Test
    public void shouldReturnGetAllRecommendationByRule() {
        //не работает
        Queries query = new Queries();
        List<Queries> queries = new ArrayList<>(List.of(query));
        Recommendations recommendationFirst = new Recommendations(
                UUID.fromString(PRODUCT_ID_TOP_SAVING),
                PRODUCT_NAME_TOP_SAVING,
                DESCRIPTION_TOP_SAVING,
                queries);
        Recommendations recommendationSecond = new Recommendations(
                UUID.fromString(PRODUCT_ID_INVEST_500),
                PRODUCT_NAME_INVEST_500,
                DESCRIPTION_INVEST_500,
                queries);
        List<Recommendations> recommendations = new ArrayList<>(List.of(recommendationFirst, recommendationSecond));
        Mockito.when(recommendationsRepository.findAll()).thenReturn(recommendations);

        out.getAllRecommendationByRule();
        Assertions.assertThat(out.getAllRecommendationByRule()).hasSize(2);
    }




}
