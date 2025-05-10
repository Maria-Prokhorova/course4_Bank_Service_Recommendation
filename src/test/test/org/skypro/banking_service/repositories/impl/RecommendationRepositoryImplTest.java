package org.skypro.banking_service.repositories.impl;

import org.junit.jupiter.api.Test;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ComponentScan(basePackages = "org.skypro.banking_service.repository.impl")
class RecommendationRepositoryImplTest {

    @Autowired
    private UserTransactionRepository recommendationRepository;

    // Получить типы продуктов, которыми пользуется пользователь
    @Test
    void shouldReturnUsedProductTypesByUserId() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        List<String> types = recommendationRepository.findUsedProductTypesByUserId(userId);

        assertThat(types).isNotEmpty();
        assertThat(types).contains("DEBIT");
    }

    // Сумма пополнений по дебетовой карте
    @Test
    void shouldReturnTotalDepositForDebit() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        Long deposit = recommendationRepository.findTotalDepositByUserIdAndProductType(userId, "DEBIT");

        assertThat(deposit).isGreaterThan(0);
    }

    // Наличие хотя бы одного продукта типа DEBIT
    @Test
    void shouldReturnExistsDebitProduct() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");
        boolean exists = recommendationRepository.existsUserProductByType(userId, "DEBIT");

        assertThat(exists).isTrue();
    }

}