package org.skypro.banking_service.service;

import org.junit.jupiter.api.Test;
import org.skypro.banking_service.dto.RecommendationDTO;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.service.impl.RecommendationForClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.skypro.banking_service.constants.ConstantsForStaticRules.*;

@SpringBootTest
public class RecommendationForClientServiceImplTest {

    @Autowired
    RecommendationForClientServiceImpl recommendationService;

    @Test
    void shouldReturnListOfRecommendations() {
        UUID userId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");
        RecommendationResponse recommendations = recommendationService.getRecommendationsForClient(userId);
        List<RecommendationDTO> recommendationList = List.of(
                new RecommendationDTO(
                PRODUCT_NAME_TOP_SAVING,
                PRODUCT_ID_TOP_SAVING,
                DESCRIPTION_TOP_SAVING
                )
        );
        assertEquals(new RecommendationResponse(userId, recommendationList), recommendations);
    }

    @Test
    void shouldReturnEmptyListOfRecommendations() {
        UUID userId = UUID.fromString("77a06779-2720-4dfb-9a06-336e8c861629");
        RecommendationResponse recommendations = recommendationService.getRecommendationsForClient(userId);
        List<RecommendationDTO> recommendationList = List.of();
        assertEquals(new RecommendationResponse(userId, recommendationList), recommendations);
    }

    @Test
    void shouldReturnUserNotFoundException() {
        UUID userId = UUID.fromString("77a06779-2720-4dfb-9a06-336e8c861639");
        //RecommendationResponse recommendations = recommendationService.getRecommendations(userId);
        assertThrows(UserNotFoundException.class, () -> recommendationService.getRecommendationsForClient(userId));
    }
}
