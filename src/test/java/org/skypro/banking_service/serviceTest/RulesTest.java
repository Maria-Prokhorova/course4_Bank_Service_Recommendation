package org.skypro.banking_service.serviceTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.service.rules.Invest500Rule;
import org.skypro.banking_service.service.rules.SimpleCreditRule;
import org.skypro.banking_service.service.rules.TopSavingRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.skypro.banking_service.constants.ProductConstants.*;

@SpringBootTest
public class RulesTest {

    @Autowired
    Invest500Rule invest500Rule;

    @Autowired
    TopSavingRule topSavingRule;

    @Autowired
    SimpleCreditRule simpleCreditRule;


    @Test
    void shouldReturnRecommendationInvest500() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

        Optional<RecommendationDto> result = invest500Rule.checkOut(userId);
        assertEquals(new RecommendationDto(
                PRODUCT_NAME_INVEST_500,
                PRODUCT_ID_INVEST_500,
                DESCRIPTION_INVEST_500), result.get());
    }

    @Test
    void shouldReturnRecommendationTopSaving() {
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");

        Optional<RecommendationDto> result = topSavingRule.checkOut(userId);
        assertEquals(new RecommendationDto(
                PRODUCT_NAME_TOP_SAVING,
                PRODUCT_ID_TOP_SAVING,
                DESCRIPTION_TOP_SAVING), result.get());
    }

    @Test
    void shouldReturnRecommendationSimpleCredit() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        Optional<RecommendationDto> result = simpleCreditRule.checkOut(userId);
        assertEquals(new RecommendationDto(
                PRODUCT_NAME_SIMPLE_CREDIT,
                PRODUCT_ID_SIMPLE_CREDIT,
                DESCRIPTION_SIMPLE_CREDIT), result.get());
    }

    @Test
    void shouldReturnEmptyRecommendation() {
        UUID userId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        Optional<RecommendationDto> result = invest500Rule.checkOut(userId);
        Assertions.assertTrue(result.isEmpty());
    }
}
