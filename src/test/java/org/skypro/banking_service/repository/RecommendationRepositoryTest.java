package org.skypro.banking_service.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class RecommendationRepositoryTest {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Test
    void shouldReturnRecommendationsFindByProductId() {

        UUID productId = UUID.randomUUID();
        Recommendation recommendations = new Recommendation();
        recommendations.setProductId(productId);
        recommendations.setProductName("test");
        recommendations.setProductText("text");
        recommendationRepository.save(recommendations);
        Recommendation result = recommendationRepository.findByProductId(productId);

        Assertions.assertNotNull(result);
    }

}
