package org.skypro.banking_service.service;

import org.skypro.banking_service.dto.RecommendationResponse;

import java.util.UUID;

public interface RecommendationForClientService {

    // Получение списка рекомендаций по банковским продуктам для заданного клиента.
    RecommendationResponse getRecommendationsForClient(UUID userId);
}
