package org.skypro.banking_service.repository;

import org.assertj.core.api.Assertions;
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
    public void shouldReturnRecommendationsByProductIdIsExists() {
        Recommendations recommend = recommendationsRepository.findByProductId(UUID.fromString(
                "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        ));

        Assertions.assertThat(recommend).isNotNull();
    }
}
