package org.skypro.banking_service.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.repositories.postgres.repository.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class RecommendationsRepositoryTest {

    @Autowired
    private RecommendationsRepository recommendationsRepository;

    @Test
    void shouldReturnRecommendationsFindByProductId() {

        UUID productId = UUID.randomUUID();
        Recommendations recommendations = new Recommendations();
        recommendations.setProductId(productId);
        recommendations.setProductName("test");
        recommendations.setProductText("text");
        recommendationsRepository.save(recommendations);
        Recommendations result = recommendationsRepository.findByProductId(productId);

        Assertions.assertNotNull(result);
    }

}
