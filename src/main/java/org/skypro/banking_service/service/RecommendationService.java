package org.skypro.banking_service.service;

import org.skypro.banking_service.dto.RecommendationResponse;

import java.util.UUID;

public interface RecommendationService {

    RecommendationResponse getRecommendations(UUID userId);
}
