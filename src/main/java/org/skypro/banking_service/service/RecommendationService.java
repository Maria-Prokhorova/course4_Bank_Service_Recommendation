package org.skypro.banking_service.service;

import org.skypro.banking_service.model.dto.RecommendationResponse;
import org.springframework.cache.annotation.CacheEvict;

import java.util.UUID;

public interface RecommendationService {

    RecommendationResponse getRecommendations(UUID userId);

    @CacheEvict(value = "recommendations", allEntries = true)
    void clearCache();
}
